package edu.hfu.scre.util;

import com.google.common.base.Joiner;
import com.google.common.io.Files;

import com.google.common.io.InsecureRecursiveDeleteException;
import com.google.common.io.MoreFiles;


import edu.hfu.scre.entity.SysNotice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件上传、移动、删除
 */
public class FileProcess {

	private static final Logger LOG = LoggerFactory.getLogger(FileProcess.class);

    Joiner joiner = Joiner.on(File.separator).skipNulls() ;
	/**
	 * 上传文件，点击上传时为临时目录传到
	 * @param file
	 * @param userCode
	 * 
	 * @return 上传的文件路径
	 * @throws IOException 
	 */
	public  String  saveFile(MultipartFile file,String userCode) throws Exception {

		//1、获取文件名字+扩展名++校验 对文件
		String fileName = file.getOriginalFilename();
		String fileExName = Files.getFileExtension(fileName);
		if(fileExName.equals("") || !checkMaterial(file,fileName,fileExName)){
			throw new java.lang.RuntimeException("文件上传时验证失败： "+ fileName);
		}

		//2、创建目录，设置保存路径
		File basePath = new File(ResourceUtils.getURL("classpath:").getPath());
		//保存路径为：static\\upfile\\temp\\2019190036
		String savePath= joiner.join("static","upfile", "temp", userCode);
		String filePath= joiner.join(basePath,savePath);
		LOG.debug("保存路径："+filePath);

		//文件重命名
		fileName = FormatUtil.cunrrentDateMillis() + Constant.SPOT + fileExName;

		//3、保存文件，要求目录要存在
		File tempDir= new File(basePath,savePath);
		tempDir.mkdirs();

		String outFilePath = joiner.join(filePath,fileName);
		FileOutputStream out = new FileOutputStream(outFilePath);
        out.write(file.getBytes());
        out.flush();
        out.close();
        
		LOG.info("上传目标文件成功："+ outFilePath);
        //返回相对路径
		return  joiner.join(savePath,fileName);
	}

	/**
	 * 1、搜索文本中的文件路径
	 * 2、替换上传到临时文件中的内容为目标路径
	 * 3、移动文件到实际目录
	 * @param nte
	 * @param userCode
	 * @param context
	 * @throws Exception
	 */
	public   void fileMove(SysNotice nte, String userCode, String context) throws Exception{
		List<String> fileTempPathList = new ArrayList<>();
        //1、从文本内容中检索出路径
		/*
		 *匹配格式为
		 * <a class="ke-insertfile" href="/acc/static/upfile/2019190036/task/20200705082043874.pdf" target="_blank">测试图片</a>
		 * <img src="/acc/static/upfile/temp/2019190036/task/20200705145015425.jpg" alt="" />
		 * <img alt="" src="/scre/static/upfile/temp/2019190036/20200917110434407.jpg" /><br />
		 * 以 class="ke-insertfile"开头，多个空格，href= 多个空格，后面双引号中间的部分 ；或者是
		 * 以 img开头，和上面匹配类似
		 */
		//String pattern = "class=\"ke-insertfile\"\\s*href=\\s*\"(.*?\\d*)\"|img\\s*src=\"(.*?\\d*)\"";
		//匹配放宽条件
		String pattern = "href\\s*=\\s*\"(.*?\\d*)\"|src\\s*=\\s*\"(.*?\\d*)\"";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(nte.getNoticeContent());
        boolean isFindFlag = false ;
		while (m.find()){
			isFindFlag = true ;
			//匹配的整体内容
			LOG.debug("Found value: " + m.group(0) );
			LOG.debug("Found value: " + m.group(1) );
			LOG.debug("Found value: " + m.group(2) );
			if(m.groupCount()<2){
				throw new RuntimeException("文本内容匹配异常");
			}
			for(int i=1; i <= m.groupCount(); i++){
				if (m.group(i) != null){
					fileTempPathList.add(m.group(i));

				}
			}
		}
		//没有传附件就不要往下走了
        if(isFindFlag==false){
			LOG.info("没有可以移动的文件：noticeId=" + nte.getNoticeId() );
            return;
		}
		LOG.info("待移动的文件路径为：" + fileTempPathList.toString());
		//2、文件移动到指定的目录中
		//保存路径：
		String basePath = ResourceUtils.getURL("classpath:").getPath();
		String savePath= joiner.join("static","upfile", userCode, nte.getNoticeTitle());
		for(String tempPath: fileTempPathList){
			//获取文件名字
			int dotIndex = tempPath.lastIndexOf(Constant.BACK_SLASH);
			String fileName = (dotIndex == -1) ? "" : tempPath.substring(dotIndex + 1);
			
			//存到数据库的内容，用新的路径替换
			String newContent = nte.getNoticeContent().replaceAll(tempPath, Joiner.on(Constant.BACK_SLASH).join(context, savePath.replace(File.separatorChar, Constant.BACK_SLASH), fileName ));
			nte.setNoticeContent(newContent);

			//要把前端需要的context去了，才能移动文件
			tempPath = tempPath.substring(context.length());
			File sourceFile = new File(basePath + tempPath);
			File destDirection = new File(joiner.join(basePath,savePath));
            destDirection.mkdirs();

			File destFile=new File(basePath + savePath+ File.separator + fileName);
            if(sourceFile.equals(destFile)){
                //修改时原来已经移动过的不再移动
               continue;
            }
			Files.move(sourceFile, destFile);
			LOG.info("移动文件成功sourceFile=" + sourceFile.getPath() + " destFile=" + destFile.getPath());
		}
	}

	/**
	 * 文件检查：大小、扩展名、二进制格式
	 * @param file    文件
	 * @param fileName 文件名字
	 * @param fileExName 文件扩展名
	 * @return
	 */
	public Boolean checkMaterial( MultipartFile file, String fileName, String fileExName ) throws IOException{

		if(file.getSize()==0 || file.getSize()>1024*1024*5) {
			throw new java.lang.RuntimeException(fileName+"大小超出上传上限5M或者等于0，size= " + file.getSize());
		}
		//先简单验证下扩展名字是否符合,拆分原有value有多个的可能
		Collection<String> fileExValuesSet = FileTypeVerify.FILE_TYPE_MAP.values();
		HashSet<String> fileExSet = new HashSet<String>();
		for(String fileExValue : fileExValuesSet){
			if(fileExValue == null){
				continue;
			}
            if(fileExValue.indexOf(Constant.SPOT) != -1){
               for(String str : fileExValue.split(Constant.SPOT_)){
				   fileExSet.add(str);
			   }
			   continue;
			}
			fileExSet.add(fileExValue);
		}
		if (!fileExSet.contains(fileExName)) {
			throw new java.lang.RuntimeException(fileName + "不是要求的格式" + fileExSet.toString());
		}
		//验证文件二进制格式，是否是真正的要求文件
		String type = FileTypeVerify.getFileType(file.getBytes());
		LOG.debug("二进制格式文件为：" + type);
		if(type == null){
			throw new java.lang.RuntimeException(fileName + "不是标准的文件格式" + fileExSet.toString());
		}
		return  true ;
	}

	/**
	 * 删除目录或文件，会递归删除下面所有的，
	 * @param pathStr 在基础路径上，再传递
	 * 无返回值，删除失败，程序继续
	 */
	public void deleteRecursively(String pathStr,  String ... args){
		String basePathStr= joiner.join("static","upfile");
		String deleteFilePath= joiner.join(basePathStr,pathStr, args);

		Path path = Paths.get(deleteFilePath);
		try {
			MoreFiles.deleteRecursively(path);
		} catch (NoSuchFileException e) {
			LOG.warn("删除文件失败，文件不存在");
			e.printStackTrace();
		}catch (InsecureRecursiveDeleteException e){
			LOG.warn("删除文件失败，文件系统安全问题，不能删除文件");
			e.printStackTrace();
		}catch (IOException e){
			LOG.warn("删除文件失败，文件IO异常");
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		//deleteRecursively("2019190036", "10");
	}
}
