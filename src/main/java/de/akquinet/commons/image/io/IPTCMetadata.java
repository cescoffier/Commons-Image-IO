package de.akquinet.commons.image.io;

import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegPhotoshopMetadata;
import org.apache.sanselan.formats.jpeg.iptc.IPTCConstants;
import org.apache.sanselan.formats.jpeg.iptc.IPTCRecord;
import org.apache.sanselan.formats.jpeg.iptc.IPTCType;
import org.apache.sanselan.formats.jpeg.iptc.PhotoshopApp13Data;

import java.util.ArrayList;
import java.util.List;

public class IPTCMetadata {

    private PhotoshopApp13Data m_photoshopApp13Data;

    public IPTCMetadata() {
        m_photoshopApp13Data = null;
    }

    public IPTCMetadata(ExtendedImageMetadata metadata, JpegImageMetadata iptc) {
        super();
        if (iptc == null) {
            return;
        }

        JpegPhotoshopMetadata psMetadata = ((JpegImageMetadata) iptc).getPhotoshop();
        if (psMetadata != null) {
            m_photoshopApp13Data = psMetadata.photoshopApp13Data;

            metadata.setTitle(getValue(IPTCConstants.IPTC_TYPE_OBJECT_NAME));
            metadata.setAuthor(getValue(IPTCConstants.IPTC_TYPE_BYLINE));
            metadata.setCity(getValue(IPTCConstants.IPTC_TYPE_CITY));
            metadata.setCopyright(getValue(IPTCConstants.IPTC_TYPE_COPYRIGHT_NOTICE));
            metadata.setCountry(getValue(IPTCConstants.IPTC_TYPE_COUNTRY_PRIMARY_LOCATION_NAME));
            metadata.setDescription(getValue(IPTCConstants.IPTC_TYPE_CAPTION_ABSTRACT));
            metadata.setEditor(getValue(IPTCConstants.IPTC_TYPE_WRITER_EDITOR));
            metadata.setExtendedAuthor(getValue(IPTCConstants.IPTC_TYPE_BYLINE_TITLE));
            metadata.setKeywords(getValues(IPTCConstants.IPTC_TYPE_KEYWORDS));
            metadata.setState(getValue(IPTCConstants.IPTC_TYPE_PROVINCE_STATE));
            metadata.setSynopsis(getValue(IPTCConstants.IPTC_TYPE_HEADLINE));
            metadata.setUsage(getValue(IPTCConstants.IPTC_TYPE_SPECIAL_INSTRUCTIONS));
            metadata.setSource(getValue(IPTCConstants.IPTC_TYPE_SOURCE));
            metadata.setCreationDate(getValue(IPTCConstants.IPTC_TYPE_DATE_CREATED));
            metadata.setContact(getValue(IPTCConstants.IPTC_TYPE_CONTACT));

        } else {
            m_photoshopApp13Data = null;
        }
    }

    public PhotoshopApp13Data getPhotoshopApp13Data(ExtendedImageMetadata metadata) {
        List<IPTCRecord> records = new ArrayList<IPTCRecord>();

        // First check if we had IPTC metadata already
        if (m_photoshopApp13Data != null) {
            records = m_photoshopApp13Data.getRecords();
        }

        setRecord(records, IPTCConstants.IPTC_TYPE_OBJECT_NAME, metadata.getTitle());
        setRecord(records, IPTCConstants.IPTC_TYPE_BYLINE, metadata.getAuthor());
        setRecord(records, IPTCConstants.IPTC_TYPE_CITY, metadata.getCity());
        setRecord(records, IPTCConstants.IPTC_TYPE_COPYRIGHT_NOTICE, metadata.getCopyright());
        setRecord(records, IPTCConstants.IPTC_TYPE_COUNTRY_PRIMARY_LOCATION_NAME, metadata.getCountry());
        setRecord(records, IPTCConstants.IPTC_TYPE_CAPTION_ABSTRACT, metadata.getDescription());
        setRecord(records, IPTCConstants.IPTC_TYPE_WRITER_EDITOR, metadata.getEditor());
        setRecord(records, IPTCConstants.IPTC_TYPE_BYLINE_TITLE, metadata.getExtendedAuthor());
        setRecord(records, IPTCConstants.IPTC_TYPE_PROVINCE_STATE, metadata.getState());
        setRecord(records, IPTCConstants.IPTC_TYPE_HEADLINE, metadata.getSynopsis());
        setRecord(records, IPTCConstants.IPTC_TYPE_SPECIAL_INSTRUCTIONS, metadata.getUsage());
        setRecord(records, IPTCConstants.IPTC_TYPE_DATE_CREATED, metadata.getCreationDate());
        setRecord(records, IPTCConstants.IPTC_TYPE_SOURCE, metadata.getSource());
        setRecords(records, IPTCConstants.IPTC_TYPE_KEYWORDS, metadata.getKeywords());
        setRecord(records, IPTCConstants.IPTC_TYPE_CONTACT, metadata.getContact());

        PhotoshopApp13Data data = null;
        if (m_photoshopApp13Data != null) {
            data = new PhotoshopApp13Data(records, m_photoshopApp13Data.getNonIptcBlocks());
        } else {
            data = new PhotoshopApp13Data(records, new ArrayList(0));
        }
        m_photoshopApp13Data = data;

        return data;

    }

    public PhotoshopApp13Data getOriginalIPTCMetadata() {
        return m_photoshopApp13Data;
    }

    public void setRecord(List<IPTCRecord> records, IPTCType type, String value) {
        IPTCRecord rec = getRecordByType(type.type);
        if (value != null) {
            // As we can't change the value of a record, we remove the record, and recreate a new one
            if (rec != null) {
                records.remove(rec);
            }
            records.add(new IPTCRecord(type, value));
        }
    }

    public void setRecords(List<IPTCRecord> records, IPTCType type, List<String> values) {
        List<IPTCRecord> rec = getRecordsByType(type.type);
        if (values != null) {
            if (rec != null) {
                records.removeAll(rec);
            }
            for (String s : values) {
                records.add(new IPTCRecord(type, s));
            }
        }
    }

    public IPTCRecord getRecordByType(int type) {
        if (m_photoshopApp13Data == null || m_photoshopApp13Data.getRecords() == null) {
            return null;
        }
        for (IPTCRecord record : (List<IPTCRecord>) m_photoshopApp13Data.getRecords()) {
            if (record.iptcType.type == type) {
                return record;
            }
        }
        return null;
    }

    public List<IPTCRecord> getRecordsByType(int type) {
        if (m_photoshopApp13Data == null || m_photoshopApp13Data.getRecords() == null) {
            return null;
        }
        List<IPTCRecord> list = new ArrayList<IPTCRecord>();
        for (IPTCRecord record : (List<IPTCRecord>) m_photoshopApp13Data.getRecords()) {
            if (record.iptcType.type == type) {
                list.add(record);
            }
        }
        return list;
    }

    public String getValue(IPTCType type) {
        IPTCRecord record = getRecordByType(type.type);
        if (record != null) {
            return record.getValue();
        } else {
            return null;
        }
    }

    public List<String> getValues(IPTCType type) {
        List<IPTCRecord> records = getRecordsByType(type.type);
        List<String> values = new ArrayList<String>();
        if (records != null) {
            for (IPTCRecord s : records) {
                values.add(s.getValue());
            }
        }
        return values;
    }
}
