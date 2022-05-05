                #include <stdio.h>
#include "Thread.h"
#include <unistd.h>

static void *PosixThreadProc(void *param)
{
	Thread *thread = (Thread *)param;
	thread->run();
	return 0;
}

Thread::Thread()
{
	setRunnableFlag(false);
	m_sleepTime = 1;
	m_isStop = false;
}

Thread::~Thread()
{
	stop();
}

void Thread::initCallback(Callback initCallback, Callback stopCallback)
{
	m_init = initCallback;
	m_stop = stopCallback;
}

bool Thread::start()
{
	setRunnableFlag(true);
	pthread_attr_t thread_attr;
	pthread_attr_init(&thread_attr);
	pthread_attr_setdetachstate(&thread_attr, PTHREAD_CREATE_DETACHED);
	if (pthread_create(&m_thread, &thread_attr, PosixThreadProc, this) != 0)
	{
		setRunnableFlag(false);
		pthread_attr_destroy(&thread_attr);
	}
	pthread_attr_destroy(&thread_attr);
	return true;
}

void Thread::stop()
{
	m_isStop = true;
}

void Thread::run()
{
	while (isRunnable())
	{
		loop();
		if (m_isStop)
		{
			if (m_stop != NULL)
			{
				m_stop(m_jniFlag);
				m_stop = NULL;
			}
			setRunnableFlag(false);
			//pthread_cancel(m_thread);
			pthread_detach(m_thread);
			m_isStop = false;
		}
		usleep((useconds_t) (m_sleepTime * 1000));
	}
}

void Thread::loop()
{
}

bool Thread::restart()
{
	stop();
	start();
	return true;
}

void Thread::setRunnableFlag(bool flag)
{
	m_runnableFlag = flag;
}

bool Thread::isRunnable()
{
	return m_runnableFlag;
}

unsigned long Thread::getThreadId()
{
	return (unsigned long)m_thread;
}
