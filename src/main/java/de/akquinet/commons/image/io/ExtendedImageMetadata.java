package de.akquinet.commons.image.io;

import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.iptc.PhotoshopApp13Data;

import java.io.IOException;
import java.util.List;

/**
 * Represents extended metadata, stored either as IPTC or XMP.
 */
public class ExtendedImageMetadata {
    
    private String m_title;
    
    private String m_author;

    private String m_contact;
    
    private String m_extendedAuthor;
    
    private String m_copyrights;
    
    private String m_usage;
    
    private String m_description;
    
    private String m_synopsis;

    private String m_editor;

    private List<String> m_keywords;

    private String m_city;

    private String m_country;
    
    private String m_state;

    private String m_creationDate;

    private String m_source;

    private boolean m_marked;
    
    private String m_webStatement;
    
    private IPTCMetadata m_iptc;
    
    private XMPMetadata m_xmp;


    public ExtendedImageMetadata() {
        m_iptc = new IPTCMetadata();
    }

    public ExtendedImageMetadata(JpegImageMetadata jpegMetadata, String xmp) throws IOException {
        m_iptc = new IPTCMetadata(this, jpegMetadata);
        m_xmp = new XMPMetadata(this, xmp);

    }
    
    public String getXMPMetadata() throws IOException {
        return m_xmp.getXMPMetadata(this);
    }

    public String getTitle() {
        return m_title;
    }

    public void setTitle(String title) {
        this.m_title = title;
    }

    public String getAuthor() {
        return m_author;
    }

    public void setAuthor(String author) {
        this.m_author = author;
    }

    public String getExtendedAuthor() {
        return m_extendedAuthor;
    }

    public void setExtendedAuthor(String extendedAuthor) {
        this.m_extendedAuthor = extendedAuthor;
    }

    public String getCopyright() {
        return m_copyrights;
    }

    public void setCopyright(String copyrights) {
        this.m_copyrights = copyrights;
        if (m_copyrights != null  && m_copyrights.length() > 0) {
            setMarked(true);
        }
    }

    public String getUsage() {
        return m_usage;
    }

    public void setUsage(String usage) {
        this.m_usage = usage;
    }

    public String getDescription() {
        return m_description;
    }

    public void setDescription(String description) {
        this.m_description = description;
    }

    public String getSynopsis() {
        return m_synopsis;
    }

    public void setSynopsis(String caption) {
        this.m_synopsis = caption;
    }

    public String getEditor() {
        return m_editor;
    }

    public void setEditor(String editor) {
        this.m_editor = editor;
    }

    public List<String> getKeywords() {
        return m_keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.m_keywords = keywords;
    }

    public String getCity() {
        return m_city;
    }

    public void setCity(String city) {
        this.m_city = city;
    }

    public String getCountry() {
        return m_country;
    }

    public void setCountry(String country) {
        this.m_country = country;
    }

    public String getState() {
        return m_state;
    }

    public void setState(String state) {
        this.m_state = state;
    }

    public String getCreationDate() {
        return m_creationDate;
    }

    public void setCreationDate(String date) {
        this.m_creationDate = date;
    }

    public IPTCMetadata getIPTC() {
        return m_iptc;
    }
    
    public void setMarked(boolean marked) {
        this.m_marked = marked; 
    }
    
    public boolean isMarked() {
        return m_marked;
    }
    
    public void setSource(String source) {
        this.m_source = source;
    }
    
    public String getSource() {
        return m_source;
    }

    public void setWebStatement(String web) {
        this.m_webStatement = web;
    }

    public String getWebStatement() {
        return m_webStatement;
    }

    public PhotoshopApp13Data getPhotoshopApp13Data() {
        return m_iptc.getPhotoshopApp13Data(this);
    }

    public void setContact(String contact) {
        this.m_contact = contact;
    }
    
    public String getContact() {
        return m_contact;
    }
}
