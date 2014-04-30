package fr.matelli.GoogleService.googleService.utils;

/**
 * Utilities for dealing with MIME types.
 */
public enum MimeUtils {

    ODS("application/vnd.oasis.opendocument.spreadsheet"),
    ODT("application/vnd.oasis.opendocument.text"),
    DOC("application/msword"),
    DOT("application/msword"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    DOTX("application/vnd.openxmlformats-officedocument.wordprocessingml.template"),
    XLS("application/vnd.ms-excel"),
    XLT("application/vnd.ms-excel"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    XLTX("application/vnd.openxmlformats-officedocument.spreadsheetml.template"),
    PPT("application/vnd.ms-powerpoint"),
    POT("application/vnd.ms-powerpoint"),
    PPS("application/vnd.ms-powerpoint"),
    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    POTX("application/vnd.openxmlformats-officedocument.presentationml.template"),
    PPSX("application/vnd.openxmlformats-officedocument.presentationml.slideshow"),
    BMP("image/bmp"),
    GIF("image/gif"),
    JPEG("image/jpeg"),
    JPG("image/jpeg"),
    PNG("image/png"),
    PDF("application/pdf"),
    GOOGLE_DOCS_SPREADSHEET("application/vnd.google-apps.spreadsheet"),
    GOOGLE_DOCS_DOCUMENT("application/vnd.google-apps.document"),
    GOOGLE_DOCS_PRESENTATION("application/vnd.google-apps.presentation"),
    HTML("text/html"),
    TXT("text/plain"),
    RTF("application/rtf");

    private String mimeType;

    private MimeUtils(String s) {
        mimeType = s;
    }

    public String getMimeType() {
        return mimeType;
    }
}