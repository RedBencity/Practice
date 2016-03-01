package ben.practice.utils.multipart.multipart;

import java.io.File;
import java.util.Map;

/**
 * @author ZhiCheng Guo
 * @version 2014年10月7日 上午11:04:36
 */
public interface MultiPartRequest {

    void addFileUpload(String param, File file);
    
    void addStringUpload(String param, String content);
    
    Map<String,File> getFileUploads();
    
    Map<String,String> getStringUploads();
}