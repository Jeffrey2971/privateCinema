package com.utils;

/**
 * @author jeffrey
 * @ClassName: ThumbnailOutPut
 * @Description: 生成视频缩略图
 * @date: 2021/10/28 12:03 上午
 * @version:
 * @since JDK 1.8
 */

//

public class ThumbnailOutPut {

    public static void main(String[] args) {
        String command = "ffmpeg -i INPUT -vsync vfr -vf \"select=isnan(prev_selected_t)+gte(t-prev_selected_t\\,1),scale=160:90,tile=10x10\" -qscale:v 3 OUTPUT";
        System.out.println(command);
    }


}
