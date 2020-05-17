package com.changgou.util;

import com.changgou.file.FastDFSFile;
import org.csource.fastdfs.*;
import org.omg.CORBA.NameValuePair;
import org.springframework.core.io.ClassPathResource;

import java.io.*;

public class FastDFSUtil {
    /**
     * 加载Tracker连接信息
     */
    static {
        try {
            //查找classpath下的文件路径
            String filename = new ClassPathResource("fdfs_client.conf").getPath();
            //加载tracker连接信息
            ClientGlobal.init(filename);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *文件上传
     */
    public static String[] upload(FastDFSFile fastDFSFile)throws Exception{
        TrackerServer trackerServer = getTrackerServer();

        //通过TrackerServer连接信息可以获取Storage的连接信息.创建StorageClient对象存储Storage的连接信息
        StorageClient storageClient = new StorageClient(trackerServer, null);

        /**
         * 通过StorageClient访问Storage,实现文件上传,并且获取文件上传后的存储信息
         * 1.上传文件的字节数组
         * 2.文件的扩展名 比如:jpg
         * 3.附加参数
         * uploads[]
         *      upload[0]:文件上传所存储Storage组的名字    group1
         *      upload[1]:文件存储到Storage上的文件名字  M00/02/44/ffdg.jpg
         * */
        String[] uploads = storageClient.upload_file(fastDFSFile.getContent(), fastDFSFile.getExt(), null);
        return uploads;
    }

    /**
     * 获取文件信息
     * @param groupName: 文件的组名 group1
     * @param remoteFileName: 文件的存储路径名字 M00/00/00/wKgGgl6BT_qAFmhyAALqlxjG-W0885.png
     */
    public static FileInfo getFile(String groupName,String remoteFileName) throws Exception {
        TrackerServer trackerServer = getTrackerServer();

        //通过TrackerServer连接信息可以获取Storage的连接信息.创建StorageClient对象存储Storage的连接信息
        StorageClient storageClient = new StorageClient(trackerServer, null);

        return storageClient.get_file_info(groupName,remoteFileName);
    }

    /**
     * 文件下载
     * @param groupName: 文件的组名 group1
     * @param remoteFileName: 文件的存储路径名字 M00/00/00/wKgGgl6BT_qAFmhyAALqlxjG-W0885.png
     */
    public static InputStream downloadFile(String groupName, String remoteFileName) throws Exception {
        TrackerServer trackerServer = getTrackerServer();

        //通过TrackerServer连接信息可以获取Storage的连接信息.创建StorageClient对象存储Storage的连接信息
        StorageClient storageClient = new StorageClient(trackerServer, null);

        //文件下载
        byte[] bytes = storageClient.download_file(groupName, remoteFileName);
        return new ByteArrayInputStream(bytes);
    }

    /**
     * 文件删除
     * @param groupName: 文件的组名 group1
     * @param remoteFileName: 文件的存储路径名字 M00/00/00/wKgGgl6BT_qAFmhyAALqlxjG-W0885.png
     */
    public static void deleteFile(String groupName, String remoteFileName) throws Exception {
        TrackerServer trackerServer = getTrackerServer();

        //通过TrackerServer连接信息可以获取Storage的连接信息.创建StorageClient对象存储Storage的连接信息
        StorageClient storageClient = new StorageClient(trackerServer, null);

        //文件删除
        storageClient.delete_file(groupName,remoteFileName);
    }

    /**
     * 获取Storage信息
     *
     */
    public static StorageServer getStorage()throws Exception{
        //创建一个tracker访问的客户端对象TrackerClient
        TrackerClient trackerClient = new TrackerClient();

        //通过TrackerClient访问TrackerServer服务,获取连接信息
        TrackerServer trackerServer = trackerClient.getConnection();

        //获取Storage信息
        return trackerClient.getStoreStorage(trackerServer);
    }

    /**
     * 获取Storage的IP和端口信息
     *
     */
    public static ServerInfo[] getStorage(String groupName, String remoteFileName)throws Exception{
        //创建一个tracker访问的客户端对象TrackerClient
        TrackerClient trackerClient = new TrackerClient();

        //通过TrackerClient访问TrackerServer服务,获取连接信息
        TrackerServer trackerServer = trackerClient.getConnection();

        //获取Storage信息
        return trackerClient.getFetchStorages(trackerServer,groupName,remoteFileName);
    }

    /**
     * 获取Tracker的信息
     */
    public static String getTrackerInfo()throws Exception{
        TrackerServer trackerServer = getTrackerServer();
        //Tracker的IP,HTTP端口
        String ip = trackerServer.getInetSocketAddress().getHostName();
        int tracker_http_port = ClientGlobal.getG_tracker_http_port();  //8080
        String url = "http://"+ip+":"+tracker_http_port;
        return url;
    }

    /**
     * 公共代码抽取TrackerServer
     */
    public static TrackerServer getTrackerServer() throws IOException {
        //创建一个tracker访问的客户端对象TrackerClient
        TrackerClient trackerClient = new TrackerClient();

        //通过TrackerClient访问TrackerServer服务,获取连接信息
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerServer;
    }

    public static void main(String[] args) throws Exception {
        /*FileInfo fileInfo = getFile("group1", "M00/00/00/wKgGgl6BT_qAFmhyAALqlxjG-W0885.png");
        System.out.println(fileInfo.getSourceIpAddr());
        System.out.println(fileInfo.getFileSize());*/

        /*//文件下载
        InputStream is = downloadFile("group1", "M00/00/00/wKgGgl6BT_qAFmhyAALqlxjG-W0885.png");

        //将文件写入到本地磁盘
        FileOutputStream os = new FileOutputStream("D:/1.jpg");

        //定义一个缓冲区
        byte[] buffer = new byte[1024];

        while (is.read(buffer)!=-1){
            os.write(buffer);
        }
        os.flush();
        os.close();
        is.close();*/

        //deleteFile("group1", "M00/00/00/wKgGgl6BX5mAPRd-AD7C_UQZvTM880.jpg");

        //获取Storage信息
        //StorageServer storage = getStorage();
       // System.out.println(storage.getStorePathIndex());
        //System.out.println(storage.getInetSocketAddress());


        System.out.println(getTrackerInfo());
    }

}
