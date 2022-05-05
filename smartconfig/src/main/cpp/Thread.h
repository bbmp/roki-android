                               #ifndef _THREAD_H_
#define _THREAD_H_
#include <pthread.h>

typedef void (*Callback)(int);

class Thread
{
private:
	bool m_runnableFlag;

protected:
	pthread_t m_thread;
	int m_sleepTime;	// 单位为毫秒
	Callback m_init;
	Callback m_stop;
	bool m_isStop;
	int m_jniFlag;
public:
	Thread();
	virtual ~Thread();
	virtual void loop();
	virtual void stop();

	void run();
	bool start();

	bool restart();

	void setRunnableFlag(bool flag);
	bool isRunnable();
	unsigned long getThreadId();
	void initCallback(Callback initCallback, Callback stopCallback);
};

#endif

