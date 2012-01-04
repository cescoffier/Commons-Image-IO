package de.akquinet.commons.image.io;

import com.adobe.xmp.*;
import com.adobe.xmp.options.PropertyOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMPMetadata {
    
    private String m_xmp;
    
    public XMPMetadata(ExtendedImageMetadata metadata, String xmp) throws IOException {
        m_xmp = xmp;
        parseXMPMetadata(metadata);
    }

    public void parseXMPMetadata(ExtendedImageMetadata metadata) throws IOException {
        if (m_xmp == null) {
            return;
        }
        try {
            XMPMeta meta = XMPMetaFactory.parseFromString(m_xmp);

            // Merge keywords
            List<String> keywords = new ArrayList<String>();
            int numberOfKeywords = meta.countArrayItems(XMPConst.NS_DC, "subject");
            for (int i = 1; i < numberOfKeywords + 1; i++) {
                keywords.add(meta.getArrayItem(XMPConst.NS_DC, "subject", i).getValue().toString());
            }
            
            List<String> existing = metadata.getKeywords();
            if (existing == null) {            
                metadata.setKeywords(keywords);
            } else {
                // Append the metadata if not contained
                for (String k : keywords) {
                    if (! existing.contains(k)) {
                        existing.add(k);
                    }
                }
                metadata.setKeywords(existing);
            }

            // Override others 
            String description = meta.getPropertyString(XMPConst.NS_DC, "description[1]");
            if (description != null) {
                metadata.setDescription(description);
            }
            
            String title = meta.getPropertyString(XMPConst.NS_DC, "title[1]");
            if (title != null) {
                metadata.setTitle(title);
            }

            String author = meta.getPropertyString(XMPConst.NS_DC, "creator[1]");
            if (author != null) {
                metadata.setAuthor(author);
            }
            
            String copyright = meta.getPropertyString(XMPConst.NS_DC, "rights[1]");
            if (copyright != null) {
                metadata.setCopyright(copyright);
            }

            String source = meta.getPropertyString(XMPConst.NS_DC, "source[1]");
            if (source != null) {
                metadata.setSource(source);
            }

            if (meta.doesPropertyExist(XMPConst.NS_XMP_RIGHTS, "Marked")) {
                metadata.setMarked(meta.getPropertyBoolean(XMPConst.NS_XMP_RIGHTS, "Marked"));
            }

            String usage = meta.getPropertyString(XMPConst.NS_XMP_RIGHTS, "UsageTerms[1]");
            if (usage != null) {
                metadata.setUsage(usage);
            }

            String web = meta.getPropertyString(XMPConst.NS_XMP_RIGHTS, "WebStatement");
            if (web != null) {
                metadata.setWebStatement(web);
            }

            String city = meta.getPropertyString(XMPConst.NS_PHOTOSHOP, "City");
            if (city != null) {
                metadata.setCity(city);
            }

            String country = meta.getPropertyString(XMPConst.NS_PHOTOSHOP, "Country");
            if (country != null) {
                metadata.setCountry(country);
            }

            String state = meta.getPropertyString(XMPConst.NS_PHOTOSHOP, "State");
            if (state != null) {
                metadata.setState(state);
            }

            String headline = meta.getPropertyString(XMPConst.NS_PHOTOSHOP, "Headline");
            if (headline != null) {
                metadata.setSynopsis(headline);
            }

            String date = meta.getPropertyString(XMPConst.NS_PHOTOSHOP, "DateCreated");
            if (date != null) {
                metadata.setCreationDate(date);
            }

            String extAuthor = meta.getPropertyString(XMPConst.NS_PHOTOSHOP, "AuthorsPosition");
            if (extAuthor != null) {
                metadata.setExtendedAuthor(extAuthor);
            }

            String editor = meta.getPropertyString(XMPConst.NS_PHOTOSHOP, "CaptionWriter");
            if (editor != null) {
                metadata.setEditor(editor);
            }

        } catch (XMPException e) {
            throw new IOException("Can't parse XMP metadata", e);
        }
    }


    public String getXMPMetadata(ExtendedImageMetadata metadata) throws IOException {
        XMPMeta meta = null;
        if (m_xmp != null) {
            try {
                meta = XMPMetaFactory.parseFromString(m_xmp);
            } catch (XMPException e) {
                throw new IOException("Can't parse the original XMP metadata", e);
            }
        } else {
            meta = XMPMetaFactory.create();
        }

        PropertyOptions options = new PropertyOptions();
        options.setArray(true);

        try {
            if (metadata.getAuthor() != null) {
                if (! meta.doesPropertyExist(XMPConst.NS_DC, "creator")) {
                    meta.setProperty(XMPConst.NS_DC, "creator", null, options);
                }
                meta.setProperty(XMPConst.NS_DC, "creator[1]", metadata.getAuthor());
            }

            if (metadata.getTitle() != null) {
                if (! meta.doesPropertyExist(XMPConst.NS_DC, "title")) {
                    meta.setProperty(XMPConst.NS_DC, "title", null, options);
                }
                meta.setProperty(XMPConst.NS_DC, "title[1]", metadata.getTitle());
            }

            if (metadata.getDescription() != null) {
                if (! meta.doesPropertyExist(XMPConst.NS_DC, "description")) {
                    meta.setProperty(XMPConst.NS_DC, "description", null, options);
                }
                meta.setProperty(XMPConst.NS_DC, "description[1]", metadata.getDescription());
            }

            if (metadata.getSource() != null) {
                if (! meta.doesPropertyExist(XMPConst.NS_DC, "source")) {
                    meta.setProperty(XMPConst.NS_DC, "source", null, options);
                }
                meta.setProperty(XMPConst.NS_DC, "source[1]", metadata.getSource());
            }

            if (metadata.getCopyright() != null) {
                if (! meta.doesPropertyExist(XMPConst.NS_DC, "rights")) {
                    meta.setProperty(XMPConst.NS_DC, "rights", null, options);
                }
                meta.setProperty(XMPConst.NS_DC, "rights[1]", metadata.getCopyright());
            }

            if (metadata.getWebStatement() != null) {
                meta.setProperty(XMPConst.NS_XMP_RIGHTS, "WebStatement", metadata.getWebStatement());
            }

            if (metadata.getUsage() != null) {
                if (! meta.doesPropertyExist(XMPConst.NS_XMP_RIGHTS, "UsageTerms")) {
                    meta.setProperty(XMPConst.NS_XMP_RIGHTS, "UsageTerms", null, options);
                }
                meta.setProperty(XMPConst.NS_XMP_RIGHTS, "UsageTerms[1]", metadata.getUsage());
            }

            meta.setProperty(XMPConst.NS_XMP_RIGHTS, "Marked",metadata.isMarked());


            if (metadata.getKeywords() != null) {
                // Recreate the array
                meta.deleteProperty(XMPConst.NS_DC, "subject");

                meta.setProperty(XMPConst.NS_DC, "subject", null, options);
                for (String k : metadata.getKeywords()) {
                    meta.appendArrayItem(XMPConst.NS_DC, "subject", k);
                }
            }

            // Photoshop namespace
            // We use this namespace as specified in the IPTC Photo Metadata specification
            if (metadata.getCity() != null) {
                meta.setProperty(XMPConst.NS_PHOTOSHOP, "City", metadata.getCity());
            }
            if (metadata.getCountry() != null) {
                meta.setProperty(XMPConst.NS_PHOTOSHOP, "Country", metadata.getCountry());
            }
            if (metadata.getState() != null) {
                meta.setProperty(XMPConst.NS_PHOTOSHOP, "State", metadata.getState());
            }
            if (metadata.getSynopsis() != null) {
                meta.setProperty(XMPConst.NS_PHOTOSHOP, "Headline", metadata.getSynopsis());
            }
            if (metadata.getCreationDate() != null) {
                meta.setProperty(XMPConst.NS_PHOTOSHOP, "DateCreated", metadata.getCreationDate());
            }
            if (metadata.getExtendedAuthor() != null) {
                meta.setProperty(XMPConst.NS_PHOTOSHOP, "AuthorsPosition", metadata.getExtendedAuthor());
            }
            if (metadata.getEditor() != null) {
                meta.setProperty(XMPConst.NS_PHOTOSHOP, "CaptionWriter", metadata.getEditor());
            }


            // TODO Support others metadata (City, Country ...)
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            XMPMetaFactory.serialize(meta, out);
            return new String(out.toByteArray());
        } catch (XMPException e) {
            throw new IOException("Can't write XMP metadata", e);
        }

    }
}
