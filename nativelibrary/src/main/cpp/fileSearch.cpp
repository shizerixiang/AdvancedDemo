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

/***** Global Variables *****/
char dir[100] = "/home";
int const MAX_STR_LEN = 200;

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
        LOGD("File: %s",filename->d_name);
    }
}