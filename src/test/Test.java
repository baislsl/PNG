package test;

import Util.*;

/**
 * Created by baislsl on 17-7-9.
 */
public class Test {
    public static void main(String[] args){
        CRCtest();
    }


    public static void CRCtest(){
        byte[] data = {
                0x49, 0x45, 0x4e, 0x44
        };

        long result = CRC.crc(data, data.length);
        System.out.println(Long.toHexString(result));
    }

}
