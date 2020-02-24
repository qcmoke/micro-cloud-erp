package com.qcmoke.common.utils;

import java.io.*;

/**
 * @author qcmoke
 */
public class JavaSerializeUtil {

//    /**
//     * 序列化
//     *
//     * @param object
//     * @param <T>
//     * @return
//     * @throws Exception
//     */
//    public static <T extends Serializable> byte[] serialize(T object) throws Exception {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(baos);
//        oos.writeObject(object);
//        return baos.toByteArray();
//    }
//
//    /**
//     * 反序列化
//     *
//     * @param bytes
//     * @param <T>
//     * @return
//     * @throws Exception
//     */
//    public static <T extends Serializable> T unserialize(byte[] bytes) throws Exception {
//        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
//        ObjectInputStream ois = new ObjectInputStream(bais);
//        return (T) ois.readObject();
//    }
}
