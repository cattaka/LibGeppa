
package net.cattaka.libgeppa.net;

import android.hardware.usb.UsbConstants;

/**
 * Describing USB class of device. This make relation between code and names.
 * 
 * @author cattaka
 */
public enum UsbClass {
    UNKNOWN(-1), //
    APP_SPEC(UsbConstants.USB_CLASS_APP_SPEC), //
    AUDIO(UsbConstants.USB_CLASS_AUDIO), //
    CDC_DATA(UsbConstants.USB_CLASS_CDC_DATA), //
    COMM(UsbConstants.USB_CLASS_COMM), //
    CONTENT_SEC(UsbConstants.USB_CLASS_CONTENT_SEC), //
    CSCID(UsbConstants.USB_CLASS_CSCID), //
    HID(UsbConstants.USB_CLASS_HID), //
    HUB(UsbConstants.USB_CLASS_HUB), //
    MASS_STORAGE(UsbConstants.USB_CLASS_MASS_STORAGE), //
    MISC(UsbConstants.USB_CLASS_MISC), //
    PER_INTERFACE(UsbConstants.USB_CLASS_PER_INTERFACE), //
    PHYSICA(UsbConstants.USB_CLASS_PHYSICA), //
    PRINTER(UsbConstants.USB_CLASS_PRINTER), //
    STILL_IMAGE(UsbConstants.USB_CLASS_STILL_IMAGE), //
    VENDOR_SPEC(UsbConstants.USB_CLASS_VENDOR_SPEC), //
    VIDEO(UsbConstants.USB_CLASS_VIDEO), //
    WIRELESS_CONTROLLER(UsbConstants.USB_CLASS_WIRELESS_CONTROLLER);

    private int classId;

    private UsbClass(int classId) {
        this.classId = classId;
    }

    public int getClassId() {
        return classId;
    }

    public static UsbClass parce(int classId) {
        for (UsbClass uc : values()) {
            if (uc.getClassId() == classId) {
                return uc;
            }
        }
        return UNKNOWN;
    }
}
