
package net.cattaka.libgeppa.passive;

import net.cattaka.libgeppa.Constants;
import net.cattaka.libgeppa.IRawSocket;
import net.cattaka.libgeppa.data.IPacket;
import net.cattaka.libgeppa.data.IPacketFactory;
import net.cattaka.libgeppa.socket.AdkRawSocket;
import net.cattaka.libgeppa.thread.ConnectionThread;
import net.cattaka.libgeppa.thread.ConnectionThread.IRawSocketPrepareTask;
import net.cattaka.libgeppa.thread.IConnectionThreadListener;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;

public class AdkPassiveReceiver<T extends IPacket> implements IPassiveReceiver<T> {
    private String ACTION_USB_PERMISSION;

    protected static final String EXTRA_USB_DEVICE_ID = "usbDeviceId";

    private void prepareCompatibility(final IPacketFactory<T> packetFactory) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mAdkCompatible = new IAdkCompatible<T>(UsbManager.ACTION_USB_ACCESSORY_ATTACHED,
                    UsbManager.ACTION_USB_ACCESSORY_DETACHED) {
                @Override
                protected ConnectionThread<T> createConnectionThread(
                        IConnectionThreadListener<T> connectionThreadListener) {
                    UsbManager usbManager = (UsbManager)mContext
                            .getSystemService(Context.USB_SERVICE);
                    UsbAccessory[] accs = usbManager.getAccessoryList();
                    UsbAccessory acc = (accs != null && accs.length > 0) ? accs[0] : null;

                    if (acc != null && usbManager.hasPermission(acc)) {
                        class AdkPrepareTask implements IRawSocketPrepareTask {
                            private UsbAccessory mAccessory;

                            public AdkPrepareTask(UsbAccessory accessory) {
                                super();
                                mAccessory = accessory;
                            }

                            @Override
                            public IRawSocket prepareRawSocket() {
                                UsbManager usbManager = (UsbManager)mContext
                                        .getSystemService(Context.USB_SERVICE);

                                ParcelFileDescriptor pfd = usbManager.openAccessory(mAccessory);
                                return new AdkRawSocket(pfd, pfd.toString());
                            }
                        }
                        ;

                        return new ConnectionThread<T>(new AdkPrepareTask(acc), packetFactory,
                                connectionThreadListener, true);
                    } else if (acc != null) {
                        // Request
                        Intent intent = new Intent(ACTION_USB_PERMISSION);
                        intent.putExtra(EXTRA_USB_DEVICE_ID, acc.getModel());
                        PendingIntent pIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
                        usbManager.requestPermission(acc, pIntent);

                        return null;
                    } else {
                        return null;
                    }
                }
            };
        } else {
            mAdkCompatible = new IAdkCompatible<T>(
                    UsbManager.ACTION_USB_ACCESSORY_ATTACHED,
                    UsbManager.ACTION_USB_ACCESSORY_DETACHED) {
                @Override
                protected ConnectionThread<T> createConnectionThread(
                        IConnectionThreadListener<T> connectionThreadListener) {
                    UsbManager usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
                    UsbAccessory[] accs = usbManager.getAccessoryList();
                    UsbAccessory acc = (accs != null && accs.length > 0) ? accs[0]
                            : null;

                    if (acc != null && usbManager.hasPermission(acc)) {
                        class AdkPrepareTask implements IRawSocketPrepareTask {
                            private UsbAccessory mAccessory;

                            public AdkPrepareTask(UsbAccessory accessory) {
                                super();
                                mAccessory = accessory;
                            }

                            @Override
                            public IRawSocket prepareRawSocket() {
                                UsbManager usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);

                                ParcelFileDescriptor pfd = usbManager.openAccessory(mAccessory);
                                return new AdkRawSocket(pfd, pfd.toString());
                            }
                        }
                        ;

                        return new ConnectionThread<T>(new AdkPrepareTask(acc), packetFactory,
                                connectionThreadListener, true);
                    } else if (acc != null) {
                        // Request
                        Intent intent = new Intent(ACTION_USB_PERMISSION);
                        intent.putExtra(EXTRA_USB_DEVICE_ID, acc.getModel());
                        PendingIntent pIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
                        usbManager.requestPermission(acc, pIntent);

                        return null;
                    } else {
                        return null;
                    }
                }
            };
        }
    }

    private IAdkCompatible<T> mAdkCompatible;

    private Context mContext;

    @SuppressWarnings("unused")
    private IPacketFactory<T> mPacketFactory;

    private IPassiveReceiverListener mListener;

    private abstract static class IAdkCompatible<T extends IPacket> {
        public final String ACTION_USB_ACCESSORY_ATTACHED;

        public final String ACTION_USB_ACCESSORY_DETACHED;

        public IAdkCompatible(String actionUsbAccessoryAttached, String actionUsbAccessoryDetached) {
            super();
            ACTION_USB_ACCESSORY_ATTACHED = actionUsbAccessoryAttached;
            ACTION_USB_ACCESSORY_DETACHED = actionUsbAccessoryDetached;
        }

        protected abstract ConnectionThread<T> createConnectionThread(
                IConnectionThreadListener<T> connectionThreadListener);
    }

    public AdkPassiveReceiver(IPassiveReceiverListener listener) {
        mListener = listener;
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(Constants.TAG, intent.toString());
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {

                mListener.startConnectionThread();
            } else if (mAdkCompatible.ACTION_USB_ACCESSORY_ATTACHED.equals(action)) {
                // UsbAccessory accessory =
                // (UsbAccessory)intent.getParcelableExtra(EXTRA_ACCESSORY);
                mListener.startConnectionThread();
            } else if (mAdkCompatible.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
                // UsbAccessory accessory =
                // (UsbAccessory)intent.getParcelableExtra(EXTRA_ACCESSORY);
                mListener.stopConnectionThread();
            }
        }
    };

    @Override
    public void onCreateService(Context context, IPacketFactory<T> packetFactory) {
        ACTION_USB_PERMISSION = context.getPackageName() + ".action_permission";
        mContext = context;
        mPacketFactory = packetFactory;

        prepareCompatibility(packetFactory);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(mAdkCompatible.ACTION_USB_ACCESSORY_DETACHED);
        context.registerReceiver(mUsbReceiver, filter);
    }

    @Override
    public void teardown() {
        if (mContext != null) {
            mContext.unregisterReceiver(mUsbReceiver);
            mContext = null;
        }
    }

    @Override
    public ConnectionThread<T> createConnectionThread(
            IConnectionThreadListener<T> connectionThreadListener) {
        return mAdkCompatible.createConnectionThread(connectionThreadListener);
    }

}
