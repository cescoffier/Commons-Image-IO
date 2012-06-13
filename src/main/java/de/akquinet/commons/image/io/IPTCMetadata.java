package de.akquinet.commons.image.io;

import org.apache.commons.imaging.common.IImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegPhotoshopMetadata;
import org.apache.commons.imaging.formats.jpeg.iptc.*;

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

            
            metadata.setTitle(getValue(IptcTypes.OBJECT_NAME));
            metadata.setAuthor(getValue(IptcTypes.BYLINE));
            metadata.setCity(getValue(IptcTypes.CITY));
            metadata.setCopyright(getValue(IptcTypes.COPYRIGHT_NOTICE));
            metadata.setCountry(getValue(IptcTypes.COUNTRY_PRIMARY_LOCATION_NAME));
            metadata.setDescription(getValue(IptcTypes.CAPTION_ABSTRACT));
            metadata.setEditor(getValue(IptcTypes.WRITER_EDITOR));
            metadata.setExtendedAuthor(getValue(IptcTypes.BYLINE_TITLE));
            metadata.setKeywords(getValues(IptcTypes.KEYWORDS));
            metadata.setState(getValue(IptcTypes.PROVINCE_STATE));
            metadata.setSynopsis(getValue(IptcTypes.HEADLINE));
            metadata.setUsage(getValue(IptcTypes.SPECIAL_INSTRUCTIONS));
            metadata.setSource(getValue(IptcTypes.SOURCE));
            metadata.setCreationDate(getValue(IptcTypes.DATE_CREATED));
            metadata.setContact(getValue(IptcTypes.CONTACT));

        } else {
            m_photoshopApp13Data = null;
        }
    }

    public PhotoshopApp13Data getPhotoshopApp13Data(ExtendedImageMetadata metadata) {
        List<IptcRecord> records = new ArrayList<IptcRecord>();

        // First check if we had IPTC metadata already
        if (m_photoshopApp13Data != null) {
            records = m_photoshopApp13Data.getRecords();
        }

        setRecord(records, IptcTypes.OBJECT_NAME, metadata.getTitle());
        setRecord(records, IptcTypes.BYLINE, metadata.getAuthor());
        setRecord(records, IptcTypes.CITY, metadata.getCity());
        setRecord(records, IptcTypes.COPYRIGHT_NOTICE, metadata.getCopyright());
        setRecord(records, IptcTypes.COUNTRY_PRIMARY_LOCATION_NAME, metadata.getCountry());
        setRecord(records, IptcTypes.CAPTION_ABSTRACT, metadata.getDescription());
        setRecord(records, IptcTypes.WRITER_EDITOR, metadata.getEditor());
        setRecord(records, IptcTypes.BYLINE_TITLE, metadata.getExtendedAuthor());
        setRecord(records, IptcTypes.PROVINCE_STATE, metadata.getState());
        setRecord(records, IptcTypes.HEADLINE, metadata.getSynopsis());
        setRecord(records, IptcTypes.SPECIAL_INSTRUCTIONS, metadata.getUsage());
        setRecord(records, IptcTypes.DATE_CREATED, metadata.getCreationDate());
        setRecord(records, IptcTypes.SOURCE, metadata.getSource());
        setRecords(records, IptcTypes.KEYWORDS, metadata.getKeywords());
        setRecord(records, IptcTypes.CONTACT, metadata.getContact());

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

    public void setRecord(List<IptcRecord> records, IptcType type, String value) {
        IptcRecord rec = getRecordByType(type.getType());
        if (value != null) {
            // As we can't change the value of a record, we remove the record, and recreate a new one
            if (rec != null) {
                records.remove(rec);
            }
            records.add(new IptcRecord(type, value));
        }
    }

    public void setRecords(List<IptcRecord> records, IptcType type, List<String> values) {
        List<IptcRecord> rec = getRecordsByType(type.getType());
        if (values != null) {
            if (rec != null) {
                records.removeAll(rec);
            }
            for (String s : values) {
                records.add(new IptcRecord(type, s));
            }
        }
    }

    public IptcRecord getRecordByType(int type) {
        if (m_photoshopApp13Data == null || m_photoshopApp13Data.getRecords() == null) {
            return null;
        }
        for (IptcRecord record : (List<IptcRecord>) m_photoshopApp13Data.getRecords()) {
            if (record.iptcType.getType() == type) {
                return record;
            }
        }
        return null;
    }

    public List<IptcRecord> getRecordsByType(int type) {
        if (m_photoshopApp13Data == null || m_photoshopApp13Data.getRecords() == null) {
            return null;
        }
        List<IptcRecord> list = new ArrayList<IptcRecord>();
        for (IptcRecord record : (List<IptcRecord>) m_photoshopApp13Data.getRecords()) {
            if (record.iptcType.getType() == type) {
                list.add(record);
            }
        }
        return list;
    }

    public String getValue(IptcType type) {
        IptcRecord record = getRecordByType(type.getType());
        if (record != null) {
            return record.getValue();
        } else {
            return null;
        }
    }

    public List<String> getValues(IptcType type) {
        List<IptcRecord> records = getRecordsByType(type.getType());
        List<String> values = new ArrayList<String>();
        if (records != null) {
            for (IptcRecord s : records) {
                values.add(s.getValue());
            }
        }
        return values;
    }
}
