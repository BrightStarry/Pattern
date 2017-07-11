package com.zx.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合模式测试类
 */
public class CompositeTest {

    public static void main(String[] args) {
        //根节点
        IFile root = new Folder("根节点",null);
        //A文件夹
        IFile AFolder = root.createFile("A文件夹");
        //创建a1文件
        IFile a1 = AFolder.createFile("a1");
        //创建a2文件
        IFile a2 = AFolder.createFile("a2");
        //列出根基点下的文件
        root.listFile();
        //列出A文件夹下的文件
        AFolder.listFile();
        //删除a1
        a1.delete();
        //列出A文件夹下的文件
        AFolder.listFile();
    }
}

/**
 * 通用接口-文件接口
 */
interface IFile {
    /**
     * 通用接口-两种节点都需实现
     */
    String getName();//获取文件名
    void delete();//删除文件

    /**
     * 非叶子节点需要实现
     */
    void deleteByName(String name);//删除单个文件,根据name
    IFile createFile(String name);//创建文件
    void listFile();//列出文件列表
    IFile getFile(int i);//获取某个文件，根据list集合下标
}

/**
 * 非叶子类- 文件夹
 */
class Folder implements IFile {
    private String name;;//名字
    private List<IFile> list = new ArrayList<>();//该文件夹下所有的文件集合
    private IFile folder;//上级目录，如果是根节点，则为空
    private Boolean isFile = false;//是否为文件，该类默认为false

    //获取文件名字
    @Override
    public String getName() {
        return this.name;
    }
    //删除所有文件
    @Override
    public void delete() {
        //先删除其下所有 文件/文件夹
        for (IFile tempFile : list) {
            tempFile.delete();
        }
        //再调用父级引用删除自己
        if(folder == null)
            throw new RuntimeException("不能删除根节点");
        folder.deleteByName(name);
    }
    //删除文件,根据文件名
    @Override
    public void deleteByName(String name) {
        for (IFile tempFile : list) {
            if (tempFile.getName().equals(name)) {
                list.remove(tempFile);
                break;
            }
        }
    }
    //创建新的文件/文件夹
    @Override
    public IFile createFile(String name) {
        IFile file = null;
        //如果是文件，创建文件
        if (name.contains(".")) {
            list.add(file = new File(name,this));
        }else{
            //否则创建文件加
            list.add(file = new Folder(name,this));
        }
        return file;
    }
    //列出所有文件
    @Override
    public void listFile() {
        for (IFile tempFile : list) {
            System.out.println(tempFile.getName());
        }
        System.out.println("---------------");
    }
    //根据下标获取某个文件
    @Override
    public IFile getFile(int i) {
        return list.get(i);
    }
    //创建时传入名字和上级的引用
    public Folder(String name,IFile folder) {
        this.name = name;
        this.folder = folder;
    }
}

/**
 * 叶子节点-文件
 */
class File implements IFile {
    private String name;//名字
    private IFile folder;//上级文件夹引用,不能为空


    public File(String name,IFile folder) {
        this.name = name;
        this.folder = folder;
    }
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void delete() {
        folder.deleteByName(name);
    }

    /**
     * 下列方法 文件类不实现
     */
    @Override
    public void deleteByName(String name) {
        throwException();
    }

    @Override
    public IFile createFile(String name) {
        throwException();
        return  null;
    }

    @Override
    public void listFile() {
        throwException();
    }

    @Override
    public IFile getFile(int i) {
        throwException();
        return null;
    }
    private void throwException(){
        throw new NullPointerException();
    }

}

