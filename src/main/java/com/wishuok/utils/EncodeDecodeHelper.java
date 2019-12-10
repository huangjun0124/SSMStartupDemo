package com.wishuok.utils;

import io.seruco.encoding.base62.Base62;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class EncodeDecodeHelper {
    private static final String _charSet = "UTF-8";

    private static byte[] gzipCompress(String str, String encoding) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(encoding));
            gzip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    private static String base62Encode(byte[] bytes) {
        Base62 base62 = Base62.createInstance();
        final byte[] encoded = base62.encode(bytes);
        try {
            return new String(encoded, _charSet);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encode(String inStr) {
        byte[] gzipBytes = gzipCompress(inStr, _charSet);
        return base62Encode(gzipBytes);
    }

    public static String decode(String inStr) {
        try {
            byte[] inBytes = inStr.getBytes(_charSet);
            byte[] base62Bytes = base62Decode(inBytes);
            return gzipDecompress(base62Bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] base62Decode(byte[] bytes) {
        Base62 base62 = Base62.createInstance();
        final byte[] decode = base62.decode(bytes);
        return decode;
    }

    private static String gzipDecompress(byte[] bytes) {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream gzip = new GZIPInputStream(in);
            return IOUtils.toString(gzip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
