package net.cattaka.libgeppa.binder.async;

import java.util.HashMap;
import java.util.Map;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Handler.Callback;

import net.cattaka.libgeppa.binder.ActiveGeppaServiceFuncs;

public class ActiveGeppaServiceFuncsAsync implements ActiveGeppaServiceFuncs {
    private static final int WORK_SIZE = 5;
    private static final int POOL_SIZE = 10;
    private static final int EVENT_START = 1;


    private static final int EVENT_METHOD_0_connect = EVENT_START + 1;
    private static final int EVENT_METHOD_1_disconnect = EVENT_START + 2;
    private static final int EVENT_METHOD_2_getCurrentDeviceInfo = EVENT_START + 3;
    private static final int EVENT_METHOD_3_registerServiceListener = EVENT_START + 4;
    private static final int EVENT_METHOD_4_sendPacket = EVENT_START + 5;
    private static final int EVENT_METHOD_5_unregisterServiceListener = EVENT_START + 6;
    private static final int EVENT_METHOD_6_asBinder = EVENT_START + 7;

    private static Callback sCallback = new Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {

                case EVENT_METHOD_0_connect: {
                    Object[] work = (Object[]) msg.obj;

                    ActiveGeppaServiceFuncs orig = (ActiveGeppaServiceFuncs) work[1];

                    net.cattaka.libgeppa.data.DeviceInfo arg0 = (net.cattaka.libgeppa.data.DeviceInfo) (work[0+2]);

                    try {

                        orig.connect(arg0);

                    } catch (Exception e) {
                        work[WORK_SIZE - 1] = e;
                    }
                    synchronized (work) {
                        work.notify();
                    }
                    return true;

                }

                case EVENT_METHOD_1_disconnect: {
                    Object[] work = (Object[]) msg.obj;

                    ActiveGeppaServiceFuncs orig = (ActiveGeppaServiceFuncs) work[1];


                    try {

                        orig.disconnect();

                    } catch (Exception e) {
                        work[WORK_SIZE - 1] = e;
                    }
                    synchronized (work) {
                        work.notify();
                    }
                    return true;

                }

                case EVENT_METHOD_2_getCurrentDeviceInfo: {
                    Object[] work = (Object[]) msg.obj;

                    ActiveGeppaServiceFuncs orig = (ActiveGeppaServiceFuncs) work[1];


                    try {

                        Object result = 
                        orig.getCurrentDeviceInfo();

                        work[WORK_SIZE - 2] = result;
                    } catch (Exception e) {
                        work[WORK_SIZE - 1] = e;
                    }
                    synchronized (work) {
                        work.notify();
                    }
                    return true;

                }

                case EVENT_METHOD_3_registerServiceListener: {
                    Object[] work = (Object[]) msg.obj;

                    ActiveGeppaServiceFuncs orig = (ActiveGeppaServiceFuncs) work[1];

                    net.cattaka.libgeppa.IActiveGeppaServiceListener arg0 = (net.cattaka.libgeppa.IActiveGeppaServiceListener) (work[0+2]);

                    try {

                        Object result = 
                        orig.registerServiceListener(arg0);

                        work[WORK_SIZE - 2] = result;
                    } catch (Exception e) {
                        work[WORK_SIZE - 1] = e;
                    }
                    synchronized (work) {
                        work.notify();
                    }
                    return true;

                }

                case EVENT_METHOD_4_sendPacket: {
                    Object[] work = (Object[]) msg.obj;

                    ActiveGeppaServiceFuncs orig = (ActiveGeppaServiceFuncs) work[1];

                    net.cattaka.libgeppa.data.PacketWrapper arg0 = (net.cattaka.libgeppa.data.PacketWrapper) (work[0+2]);

                    try {

                        Object result = 
                        orig.sendPacket(arg0);

                        work[WORK_SIZE - 2] = result;
                    } catch (Exception e) {
                        work[WORK_SIZE - 1] = e;
                    }
                    synchronized (work) {
                        work.notify();
                    }
                    return true;

                }

                case EVENT_METHOD_5_unregisterServiceListener: {
                    Object[] work = (Object[]) msg.obj;

                    ActiveGeppaServiceFuncs orig = (ActiveGeppaServiceFuncs) work[1];

                    Integer arg0 = (Integer) (work[0+2]);

                    try {

                        Object result = 
                        orig.unregisterServiceListener(arg0);

                        work[WORK_SIZE - 2] = result;
                    } catch (Exception e) {
                        work[WORK_SIZE - 1] = e;
                    }
                    synchronized (work) {
                        work.notify();
                    }
                    return true;

                }

                case EVENT_METHOD_6_asBinder: {
                    Object[] work = (Object[]) msg.obj;

                    ActiveGeppaServiceFuncs orig = (ActiveGeppaServiceFuncs) work[1];


                    try {

                        Object result = 
                        orig.asBinder();

                        work[WORK_SIZE - 2] = result;
                    } catch (Exception e) {
                        work[WORK_SIZE - 1] = e;
                    }
                    synchronized (work) {
                        work.notify();
                    }
                    return true;

                }

            }
            return false;
        }
    };
    private static Map<Looper, Handler> sHandlerMap = new HashMap<Looper, Handler>();
    private Handler mHandler;
    private Object[][] sOwnedPool = new Object[POOL_SIZE][WORK_SIZE];
    private ActiveGeppaServiceFuncs orig;

    public ActiveGeppaServiceFuncsAsync(ActiveGeppaServiceFuncs orig, Looper looper) {
        super();
        this.orig = orig;
        synchronized (sHandlerMap) {
            mHandler = sHandlerMap.get(looper);
            if (mHandler == null) {
                mHandler = new Handler(looper, sCallback);
                sHandlerMap.put(looper, mHandler);
            }
        }
    }

    public ActiveGeppaServiceFuncsAsync(ActiveGeppaServiceFuncs orig) {
        this(orig, Looper.getMainLooper());
    }



    @Override
    public  void connect(net.cattaka.libgeppa.data.DeviceInfo arg0) 

            throws

        android.os.RemoteException

    {
    	if (Looper.myLooper() == mHandler.getLooper()) {

    		orig.connect(arg0);
            return;

    	}
        Object[] work = obtain();
        work[0] = this;
        work[1] = orig;

        work[0+2] = arg0;

        synchronized (work) {
            mHandler.obtainMessage(EVENT_METHOD_0_connect, work)
                    .sendToTarget();
            try {
                work.wait();
            } catch (InterruptedException e) {
                throw new AsyncInterfaceException(e);
            }
        }
        if (work[WORK_SIZE - 1] != null) {


            
            if (work[WORK_SIZE - 1] instanceof android.os.RemoteException) {
                throw (android.os.RemoteException) work[WORK_SIZE - 1];

            } else {
                throw new AsyncInterfaceException((Exception) work[WORK_SIZE - 1]);
            }

        }



    }

    @Override
    public  void disconnect() 

            throws

        android.os.RemoteException

    {
    	if (Looper.myLooper() == mHandler.getLooper()) {

    		orig.disconnect();
            return;

    	}
        Object[] work = obtain();
        work[0] = this;
        work[1] = orig;


        synchronized (work) {
            mHandler.obtainMessage(EVENT_METHOD_1_disconnect, work)
                    .sendToTarget();
            try {
                work.wait();
            } catch (InterruptedException e) {
                throw new AsyncInterfaceException(e);
            }
        }
        if (work[WORK_SIZE - 1] != null) {


            
            if (work[WORK_SIZE - 1] instanceof android.os.RemoteException) {
                throw (android.os.RemoteException) work[WORK_SIZE - 1];

            } else {
                throw new AsyncInterfaceException((Exception) work[WORK_SIZE - 1]);
            }

        }



    }

    @Override
    public  net.cattaka.libgeppa.data.DeviceInfo getCurrentDeviceInfo() 

            throws

        android.os.RemoteException

    {
    	if (Looper.myLooper() == mHandler.getLooper()) {

    		return orig.getCurrentDeviceInfo();

    	}
        Object[] work = obtain();
        work[0] = this;
        work[1] = orig;


        synchronized (work) {
            mHandler.obtainMessage(EVENT_METHOD_2_getCurrentDeviceInfo, work)
                    .sendToTarget();
            try {
                work.wait();
            } catch (InterruptedException e) {
                throw new AsyncInterfaceException(e);
            }
        }
        if (work[WORK_SIZE - 1] != null) {


            
            if (work[WORK_SIZE - 1] instanceof android.os.RemoteException) {
                throw (android.os.RemoteException) work[WORK_SIZE - 1];

            } else {
                throw new AsyncInterfaceException((Exception) work[WORK_SIZE - 1]);
            }

        }


        net.cattaka.libgeppa.data.DeviceInfo result = (net.cattaka.libgeppa.data.DeviceInfo) work[WORK_SIZE - 2];
        recycle(work);


        return result;


    }

    @Override
    public  int registerServiceListener(net.cattaka.libgeppa.IActiveGeppaServiceListener arg0) 

            throws

        android.os.RemoteException

    {
    	if (Looper.myLooper() == mHandler.getLooper()) {

    		return orig.registerServiceListener(arg0);

    	}
        Object[] work = obtain();
        work[0] = this;
        work[1] = orig;

        work[0+2] = arg0;

        synchronized (work) {
            mHandler.obtainMessage(EVENT_METHOD_3_registerServiceListener, work)
                    .sendToTarget();
            try {
                work.wait();
            } catch (InterruptedException e) {
                throw new AsyncInterfaceException(e);
            }
        }
        if (work[WORK_SIZE - 1] != null) {


            
            if (work[WORK_SIZE - 1] instanceof android.os.RemoteException) {
                throw (android.os.RemoteException) work[WORK_SIZE - 1];

            } else {
                throw new AsyncInterfaceException((Exception) work[WORK_SIZE - 1]);
            }

        }


        int result = (Integer) work[WORK_SIZE - 2];
        recycle(work);


        return result;


    }

    @Override
    public  boolean sendPacket(net.cattaka.libgeppa.data.PacketWrapper arg0) 

            throws

        android.os.RemoteException

    {
    	if (Looper.myLooper() == mHandler.getLooper()) {

    		return orig.sendPacket(arg0);

    	}
        Object[] work = obtain();
        work[0] = this;
        work[1] = orig;

        work[0+2] = arg0;

        synchronized (work) {
            mHandler.obtainMessage(EVENT_METHOD_4_sendPacket, work)
                    .sendToTarget();
            try {
                work.wait();
            } catch (InterruptedException e) {
                throw new AsyncInterfaceException(e);
            }
        }
        if (work[WORK_SIZE - 1] != null) {


            
            if (work[WORK_SIZE - 1] instanceof android.os.RemoteException) {
                throw (android.os.RemoteException) work[WORK_SIZE - 1];

            } else {
                throw new AsyncInterfaceException((Exception) work[WORK_SIZE - 1]);
            }

        }


        boolean result = (Boolean) work[WORK_SIZE - 2];
        recycle(work);


        return result;


    }

    @Override
    public  boolean unregisterServiceListener(int arg0) 

            throws

        android.os.RemoteException

    {
    	if (Looper.myLooper() == mHandler.getLooper()) {

    		return orig.unregisterServiceListener(arg0);

    	}
        Object[] work = obtain();
        work[0] = this;
        work[1] = orig;

        work[0+2] = arg0;

        synchronized (work) {
            mHandler.obtainMessage(EVENT_METHOD_5_unregisterServiceListener, work)
                    .sendToTarget();
            try {
                work.wait();
            } catch (InterruptedException e) {
                throw new AsyncInterfaceException(e);
            }
        }
        if (work[WORK_SIZE - 1] != null) {


            
            if (work[WORK_SIZE - 1] instanceof android.os.RemoteException) {
                throw (android.os.RemoteException) work[WORK_SIZE - 1];

            } else {
                throw new AsyncInterfaceException((Exception) work[WORK_SIZE - 1]);
            }

        }


        boolean result = (Boolean) work[WORK_SIZE - 2];
        recycle(work);


        return result;


    }

    @Override
    public  android.os.IBinder asBinder() 

    {
    	if (Looper.myLooper() == mHandler.getLooper()) {

    		return orig.asBinder();

    	}
        Object[] work = obtain();
        work[0] = this;
        work[1] = orig;


        synchronized (work) {
            mHandler.obtainMessage(EVENT_METHOD_6_asBinder, work)
                    .sendToTarget();
            try {
                work.wait();
            } catch (InterruptedException e) {
                throw new AsyncInterfaceException(e);
            }
        }
        if (work[WORK_SIZE - 1] != null) {

            throw new AsyncInterfaceException((Exception) work[WORK_SIZE - 1]);

        }


        android.os.IBinder result = (android.os.IBinder) work[WORK_SIZE - 2];
        recycle(work);


        return result;


    }


    private Object[] obtain() {
        final Object[][] pool = sOwnedPool;
        synchronized (pool) {
            Object[] p;
            for (int i = 0; i < POOL_SIZE; i++) {
                p = pool[i];
                if (p != null) {
                    pool[i] = null;
                    return p;
                }
            }
        }
        return new Object[WORK_SIZE];
    }

    private void recycle(Object[] p) {
    	for (int i=0;i<p.length;i++) {
    		p[i] = null;
    	}
        final Object[][] pool = sOwnedPool;
        synchronized (pool) {
            for (int i = 0; i < POOL_SIZE; i++) {
                if (pool[i] == null) {
                    pool[i] = p;
                    return;
                }
            }
        }
    }
}