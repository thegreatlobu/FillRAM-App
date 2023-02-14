#include <jni.h>
#include <string>
#include<vector>
#include <android/log.h>
#include <unistd.h>
#include <memory>

#define TAG "NATIVE"
#define LOG(TAG, msg) __android_log_print(ANDROID_LOG_DEBUG, TAG, msg)

//Class that represents memory objects to be allocated
class DummyObj{
private:
    int mb;
    int blocks;
    std::vector<int> chunks;
public:
    explicit DummyObj(int mb_):mb(mb_ * 1024 * 1024), blocks(mb / 4),
    chunks(std::vector<int>(blocks, INT32_MAX)){}
    int id;
};


class DummyObjContainer{
public:
    DummyObjContainer() : container(std::vector<std::unique_ptr<DummyObj>>()) {}

    void add(DummyObj& obj) {
        std::unique_ptr<DummyObj> obj_ptr = std::make_unique<DummyObj>(obj);
        obj_ptr->id = container.size();
        container.push_back(std::move(obj_ptr));
    }

    void clear() {container.clear();}

    std::vector<std::unique_ptr<DummyObj>> container;
};

extern "C"
JNIEXPORT void JNICALL
Java_com_example_syssim_MainActivity_allocateMemory(JNIEnv *env, jobject thiz, jlong container,
                                                    jint mb) {
    DummyObjContainer *objContainer = (DummyObjContainer*) container;
    DummyObj obj(mb);
    objContainer->add(obj);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_syssim_MainActivity_deallocateMemory(JNIEnv *env, jobject thiz, jlong container) {
    DummyObjContainer *objContainer = (DummyObjContainer*) container;
    objContainer->clear();
}
extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_syssim_MainActivity_initMemoryContainer(JNIEnv *env, jobject thiz) {
    __android_log_print(ANDROID_LOG_DEFAULT, TAG,"INTIALIZED");
    //casting to long to have a reference to container in application code
    return (long)(new DummyObjContainer());
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_syssim_MainActivity_Fork(JNIEnv *env, jobject thiz) {
    int pid = fork();
    if (pid == 0){
        LOG(TAG, "CHILD PROCESS");
    }else{
        LOG(TAG, "PARENT PROCESS");
    }

}