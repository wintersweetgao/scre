package edu.hfu.scre.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;


public class FileUpload {

	private static final Logger LOG = LoggerFactory.getLogger(FileUpload.class);
	/**
	 * 
	 * @param file
	 * @param basePath //保存到基础路径
	 * @param userCode
	 * @param materialType 材料类型
	 * @param materialTitle 材料标题
	 * @param screNameTitle 科研 著作的标题
	 * 
	 * @return
	 * @throws IOException 
	 */
	public  Map<String,String> saveFile(MultipartFile[] file,String basePath,String userCode
			,String materialType,String[] materialTitle,String screNameTitle) throws IOException {
		//保存路径：
		String savePath="\\static\\upfile\\"+userCode+"\\"+materialType+"\\"+screNameTitle.trim();
		String filePath=basePath+savePath;
		LOG.debug("保存路径："+filePath);
		File upload = new File(basePath,savePath);
		if (!upload.exists()) {
			if(upload.mkdirs()) {
				LOG.debug("创建路径："+filePath+",成功");
			}else {
				LOG.debug("创建路径："+filePath+",失败");
			}
		}
		LOG.debug("文件数量："+file.length);
		Map<String,String> map=new HashMap<>();
		for(int i=0;i<file.length;i++) {
			String fileName = file[i].getOriginalFilename();
			if ("".equals(fileName)||file[i].getSize()==0||null==materialTitle[i]||"".equals(materialTitle[i])) {
				break;
			}
			LOG.debug("原始文件："+fileName);
			if(fileName.indexOf("\\") != -1){
				fileName=fileName.substring(fileName.lastIndexOf("\\")+1);
			}
			if(file[i].getSize()>1024*200) {
				throw new java.lang.RuntimeException("文件:"+fileName+"大小超出上传上限200k");
			}
			if (!(fileName.toLowerCase().endsWith(".jpg"))) {
				throw new java.lang.RuntimeException("文件:"+fileName+"不是jpg文件");
			}
			String timpStap=String.valueOf(new Date().getTime());
			
			fileName =materialTitle[i]+timpStap+".jpg";
			FileOutputStream out = null;
			if (!CheckFileType.isImage(file[i].getInputStream(),fileName)) {
        		throw new java.lang.RuntimeException("文件:"+fileName+"不是标准的图片文件");
			}
            out = new FileOutputStream(filePath+"\\"+fileName);
            out.write(file[i].getBytes());
            out.flush();
            out.close();
            map.put(materialTitle[i],(savePath+"\\"+fileName).replace('\\', '/'));
            LOG.debug("目标文件："+savePath+"\\"+fileName);
		}
		
		return map;
	}
	
	public  String  saveAdPic(MultipartFile[] file,String basePath,Integer hfirstPictureId) throws IOException{
		//保存路径：
		String savePath="\\static\\advert\\";
		String filePath=basePath+savePath;
		LOG.debug("保存路径："+filePath);
		File upload = new File(basePath,savePath);
		if (!upload.exists()) {
			if(upload.mkdirs()) {
				LOG.debug("创建路径："+filePath+",成功");
			}else {
				LOG.debug("创建路径："+filePath+",失败");
			}
		}
		LOG.debug("文件数量："+file.length);
		if (file.length==0) {
			throw new java.lang.RuntimeException("未找到上传的文件");
		}
		String fileName = file[0].getOriginalFilename();
		if ("".equals(fileName)||file[0].getSize()==0) {
			throw new java.lang.RuntimeException("文件:"+fileName+"内容为空");
		}
		LOG.debug("原始文件："+fileName);
		if(fileName.indexOf("\\") != -1){
			fileName=fileName.substring(fileName.lastIndexOf("\\")+1);
		}
		if(file[0].getSize()>1024*500) {
			throw new java.lang.RuntimeException("文件:"+fileName+"大小超出上传上限400k");
		}
		if (!(fileName.toLowerCase().endsWith(".jpg")||fileName.toLowerCase().endsWith(".png"))) {
			throw new java.lang.RuntimeException("文件:"+fileName+"不是jpg 或 png文件");
		}
		
		String extendName=fileName.substring(fileName.lastIndexOf('.'), fileName.length());
		fileName ="ad"+hfirstPictureId+extendName;
		FileOutputStream out = null;
		if (!CheckFileType.isImage(file[0].getInputStream(),fileName)) {
			throw new java.lang.RuntimeException("文件:"+fileName+"不是标准的图片文件");
		}
		BufferedImage img=CheckFileType.getImgPxSize(file[0].getInputStream());
		if (img==null) {
			throw new java.lang.RuntimeException("文件:"+fileName+"未能成功转成图片！");
		}else {
			int width=img.getWidth();
			int height=img.getHeight();
			if (width!=1181&&height!=400) {
				throw new java.lang.RuntimeException("请上传图片1181x400尺寸大小的图片，以获取最佳浏览效果");
			}
		}
		
		out = new FileOutputStream(filePath+"\\"+fileName);
		out.write(file[0].getBytes());
		out.flush();
		out.close();
		LOG.debug("目标文件："+savePath+fileName);
		
		String fulPath=(savePath+fileName).replace('\\', '/');
		return fulPath;
	}
	public static void main(String[] args) {
		String fileName="aa.jPg";
		System.out.println(fileName.endsWith(".jpg"));
		String extendName=fileName.substring(fileName.lastIndexOf('.'), fileName.length());
		System.out.println(extendName);
	}
}
