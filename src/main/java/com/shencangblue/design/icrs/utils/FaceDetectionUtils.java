package com.shencangblue.design.icrs.utils;

import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

public class FaceDetectionUtils {

    /**
     * 活体人数识别
     * @param image 欲对比图片
     * @return 识别的活体人数
     */
    public int liveDetection(File image){
        return 0;
    }

    /**
     * 获得RGB数据
     * @param file 图片文件
     * @return
     */
    public ImageInfoUtils getRGBData_E(File file) {
        if (file == null){
            return null;
        }
        ImageInfoUtils imageInfoUtil;
        try {
            //将图片文件加载到内存缓冲区
            BufferedImage image = ImageIO.read(file);
            imageInfoUtil = bufferedImage2ImageInfo(image);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return imageInfoUtil;
    }

    /**
     * 图片缓冲区处理
     * @param image
     * @return
     */
    private ImageInfoUtils bufferedImage2ImageInfo(BufferedImage image) {
        ImageInfoUtils imageInfoUtil = new ImageInfoUtils();
        int width = image.getWidth();
        int height = image.getHeight();
        // 使图片居中
        width = width & (~3);
        height = height & (~3);
        imageInfoUtil.width = width;
        imageInfoUtil.height = height;
        //根据原图片信息新建一个图片缓冲区
        BufferedImage resultImage = new BufferedImage(width, height, image.getType());
        //得到原图的rgb像素矩阵
        int[] rgb = image.getRGB(0, 0, width, height, null, 0, width);
        //将像素矩阵 绘制到新的图片缓冲区中
        resultImage.setRGB(0, 0, width, height, rgb, 0, width);
        //进行数据格式化为可用数据
        BufferedImage dstImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        if (resultImage.getType() != BufferedImage.TYPE_3BYTE_BGR) {
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB);
            ColorConvertOp colorConvertOp = new ColorConvertOp(cs, dstImage.createGraphics().getRenderingHints());
            colorConvertOp.filter(resultImage, dstImage);
        } else {
            dstImage = resultImage;
        }

        //获取rgb数据
        imageInfoUtil.rgbData = ((DataBufferByte) (dstImage.getRaster().getDataBuffer())).getData();
        return imageInfoUtil;
    }
}
