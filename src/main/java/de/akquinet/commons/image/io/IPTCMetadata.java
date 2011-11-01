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

public class IPTCMetadata extends ArrayList<IPTCMetadata.Record> {

    private boolean m_updated = false;
    private final PhotoshopApp13Data m_photoshopApp13Data;

    public IPTCMetadata() {
        super();
        m_photoshopApp13Data = null;
    }

    public IPTCMetadata(JpegImageMetadata metadata) {
        super();
        JpegPhotoshopMetadata psMetadata = ((JpegImageMetadata) metadata).getPhotoshop();
        m_photoshopApp13Data = psMetadata.photoshopApp13Data;
        List oldRecords = m_photoshopApp13Data.getRecords();
        if (oldRecords != null) {
            for (IPTCRecord record : (List<IPTCRecord>) oldRecords) {
                addMetadata(record.getIptcTypeName(), record.iptcType.type, record.getValue());
            }
        }
    }

    public PhotoshopApp13Data getPhotoshopApp13Data() {
        List<IPTCRecord> records = new ArrayList<IPTCRecord>();
        for (Record record : this) {
            for (String v : record.getValues()) {
                records.add(new IPTCRecord(record.getIPTCType(), v));
            }
        }

        return new PhotoshopApp13Data(records, m_photoshopApp13Data.getNonIptcBlocks());

    }

    public class Record {

        private final List<String> values;
        private final String name;
        private final int type;

        public Record(String name, int type, List<String> values) {
            this.type = type;
            this.values = values;
            this.name = name;
        }

        public Record(String name, int type, String value) {
            this.type = type;
            this.values = new ArrayList<String>(1);
            values.add(value);
            this.name = name;
        }

        public IPTCType getIPTCType() {
            return new IPTCType(type, name);
        }

        public List<String> getValues() {
            return new ArrayList<String>(values);
        }

        public String getName() {
            return name;
        }

        public int getType() {
            return type;
        }

        public int getNumberOfValues() {
            return values.size();
        }

        public void addValue(String value) {
            values.add(value);
        }

        public void setValue(String value) {
            values.clear();
            values.add(value);
        }

        public void setValues(List<String> values) {
            this.values.clear();
            this.values.addAll(values);
        }

        public String getValue() {
            if (!values.isEmpty()) {
                return values.get(0);
            }
            return null;
        }
    }

    public Record getRecordByName(String name) {
        for (Record record : this) {
            if (record.getName().equals(name)) {
                return record;
            }
        }
        return null;
    }

    public Record getRecordByType(int type) {
        for (Record record : this) {
            if (record.getType() == type) {
                return record;
            }
        }
        return null;
    }

    public String getValue(int type) {
        Record record = getRecordByType(type);
        if (record != null) {
            return record.getValue();
        } else {
            return null;
        }
    }

    public List<String> getValues(int type) {
        Record record = getRecordByType(type);
        if (record != null) {
            return record.getValues();
        } else {
            return new ArrayList<String>(0);
        }
    }

    public void addMetadata(String name, int type, String value) {
        Record record = getRecordByType(type);
        if (record == null) {
            add(new Record(name, type, value));
        } else {
            record.addValue(value);
        }
    }

    public void updateMetadata(IPTCType type, String value) {
        Record record = getRecordByType(type.type);
        if (record == null) {
            add(new Record(type.name, type.type, value));
        } else {
            record.setValue(value);
        }
        updated(true);
    }

    public void updateMetadata(IPTCType type, List<String> values) {
        Record record = getRecordByType(type.type);
        if (record == null) {
            add(new Record(type.name, type.type, values));
        } else {
            record.setValues(values);
        }
        updated(true);
    }

    public synchronized boolean wasUpdated() {
        return m_updated;
    }

    public synchronized void updated(boolean updated) {
        m_updated = updated;
    }

}
