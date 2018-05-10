//
// @author:BevisWang
//
#include <iostream>
#include <stdio.h>
#include <unistd.h>
#include <dirent.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <string.h>
#include "android/log.h"

static const char *TAG = "fileSearch";
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

/* Show all files under dir_name , do not show directories ! */
void showAllFiles(const char *dir_name) {
    // check the parameter !
    if (NULL == dir_name) {
        LOGE("dir_name is null.");
        return;
    }

    // check if dir_name is a valid dir
    struct stat s;
    lstat(dir_name, &s);
    if (!S_ISDIR(s.st_mode)) {
        LOGE("dir_name is not a valid directory.");
        return;
    }

    struct dirent *filename;    // return value for readdir()
    DIR *dir;                   // return value for opendir()
    dir = opendir(dir_name);
    if (NULL == dir) {
        LOGE("Can not open dir %s.", dir_name);
        return;
    }
    LOGD("Successfully opened the dir !");

    /* read all the files in the dir ~ */
    while ((filename = readdir(dir)) != NULL) {
        // get rid of "." and ".."
        if (strcmp(filename->d_name, ".") == 0 ||
            strcmp(filename->d_name, "..") == 0)
            continue;
        LOGD("File: %s", filename->d_name);
    }
}

// 定义目录扫描函数
void scan_dir(const char *dir, int depth)
{
    DIR *dp; // 定义子目录流指针
    struct dirent *entry; // 定义dirent结构指针保存后续目录
    struct stat statbuf; // 定义statbuf结构保存文件属性
    if ((dp = opendir(dir)) == NULL) // 打开目录，获取子目录流指针，判断操作是否成功
    {
        puts("can't open dir.");
        return;
    }
    chdir(dir); // 切换到当前目录
    while ((entry = readdir(dp)) != NULL) // 获取下一级目录信息，如果未否则循环
    {
        lstat(entry->d_name, &statbuf); // 获取下一级成员属性
        if (S_IFDIR & statbuf.st_mode) // 判断下一级成员是否是目录
        {
            if (strcmp(".", entry->d_name) == 0 || strcmp("..", entry->d_name) == 0)
                continue;

            LOGD("%*s%s/\n", depth, "", entry->d_name); // 输出目录名称
            scan_dir(entry->d_name, depth + 4); // 递归调用自身，扫描下一级目录的内容
        } else
            LOGD("%*s%s\n", depth, "", entry->d_name); // 输出属性不是目录的成员
    }
    chdir(".."); // 回到上级目录
    closedir(dp); // 关闭子目录流
}