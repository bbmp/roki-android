package com.robam.roki.ui.bean3;

import com.google.gson.annotations.SerializedName;

public class ImageDetail {

    @SerializedName("ColorSpace")
    public ColorSpaceDTO colorSpace;
    @SerializedName("Compression")
    public CompressionDTO compression;
    @SerializedName("DateTime")
    public DateTimeDTO dateTime;
    @SerializedName("ExifTag")
    public ExifTagDTO exifTag;
    @SerializedName("FileSize")
    public FileSizeDTO fileSize;
    @SerializedName("Format")
    public FormatDTO format;
    @SerializedName("ImageHeight")
    public ImageHeightDTO imageHeight;
    @SerializedName("ImageWidth")
    public ImageWidthDTO imageWidth;
    @SerializedName("JPEGInterchangeFormat")
    public JPEGInterchangeFormatDTO jPEGInterchangeFormat;
    @SerializedName("JPEGInterchangeFormatLength")
    public JPEGInterchangeFormatLengthDTO jPEGInterchangeFormatLength;
    @SerializedName("Orientation")
    public OrientationDTO orientation;
    @SerializedName("PixelXDimension")
    public PixelXDimensionDTO pixelXDimension;
    @SerializedName("PixelYDimension")
    public PixelYDimensionDTO pixelYDimension;
    @SerializedName("ResolutionUnit")
    public ResolutionUnitDTO resolutionUnit;
    @SerializedName("Software")
    public SoftwareDTO software;
    @SerializedName("XResolution")
    public XResolutionDTO xResolution;
    @SerializedName("YResolution")
    public YResolutionDTO yResolution;

    public static class ColorSpaceDTO {
        public String value;
    }

    public static class CompressionDTO {
        public String value;
    }

    public static class DateTimeDTO {
        public String value;
    }

    public static class ExifTagDTO {
        public String value;
    }

    public static class FileSizeDTO {
        public String value;
    }

    public static class FormatDTO {
        public String value;
    }

    public static class ImageHeightDTO {
        public String value;
    }

    public static class ImageWidthDTO {
        public String value;
    }

    public static class JPEGInterchangeFormatDTO {
        public String value;
    }

    public static class JPEGInterchangeFormatLengthDTO {
        public String value;
    }

    public static class OrientationDTO {
        public String value;
    }

    public static class PixelXDimensionDTO {
        public String value;
    }

    public static class PixelYDimensionDTO {
        public String value;
    }

    public static class ResolutionUnitDTO {
        public String value;
    }

    public static class SoftwareDTO {
        public String value;
    }

    public static class XResolutionDTO {
        public String value;
    }

    public static class YResolutionDTO {
        public String value;
    }
}
