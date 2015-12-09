## LibGeppa
LibGeppa is a library to manage connection with HW, such as ADK, USB-Host-API, Bluetooth.
This provides Android Service.
Once service is started, it manages connected/disconnected events.
To send/receive message from activity, you can bind the service and set listeneres.

This library contains 3 services. ActiveGeppaService, AdkPassiveGeppaService and BluetoothPassiveGeppaService.

### ActiveGeppaService
This provides manual connect functions.
Supports: USB-Host-API, Bluetooth, TCP/IP

#### Creating service
```java
public class GeppaServiceEx extends ActiveGeppaService<MyPacket> {
    public GeppaServiceEx() {
        super(new MyPacketFactory());
    }

    @Override
    protected void handleConnectedNotification(boolean b, DeviceInfo deviceInfo) {
        // Put code if it is needed.
    }
}
```
#### Connecting HW
```java
String devicekey = "0403:6001";
DeviceInfo deviceInfo = DeviceInfo.createUsb(devicekey, false);
mService.connect(deviceInfo);
```

#### Sending packet
```java
MyPacket packet = new MyPacket();
/* Set packet data here */
mService.sendPacket(new PacketWrapper(packet));
```

#### Receiving packet
```java
IActiveGeppaServiceListener listener = new IActiveGeppaServiceListener.Stub() {
    public void onDeviceStateChanged(in DeviceState state, in DeviceEventCode code, in DeviceInfo deviceInfo) {
        // Handle connection event here
    }
    
    public void onReceivePacket(in PacketWrapper packet) {
        // Handle received packet here
    }
}
mService.registerServiceListener(listener);
```
Note:If you use USB-Host-API, create app/src/main/res/xml/usb_device_filter.xml with your device's vendor-id and product-id.
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <usb-device vendor-id="1027" product-id="24577" />
</resources>
```


### AdkPassiveGeppaService
This provides auto connect functions for ADK.

Supports: ADK

#### Creating service
```java
public class GeppaServiceEx extends AdkPassiveGeppaService<MyPacket> {
    public GeppaServiceEx() {
        super(new MyPacketFactory());
    }
}
```
Note: Don't forget to create app/src/main/res/xml/accessory_filter.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <usb-accessory manufacturer="Your manufacturer" model="Your device model" version="1.0" />
</resources>
```

#### Sending packet
```java
MyPacket packet = new MyPacket();
/* Set packet data here */
mService.sendPacket(new PacketWrapper(packet));
```
#### Receiving packet
```java
IPassiveGeppaServiceListener listener = new IPassiveGeppaServiceListener.Stub() {
    public void onConnectionStateChanged(in ConnectionState state) {
        // Handle connection event here
    }
    public void onReceivePacket(in PacketWrapper packet) {
        // Handle received packet here
    }
}
mService.registerGeppaServiceListener(listener);
```

### BluetoothPassiveGeppaService
This provides auto connect functions for Bluetooth.

Supports: Bluetooth(Auto connect)

#### Creating service
```java
public class GeppaServiceEx extends GeppaService<MyPacket> {
    public GeppaServiceEx() {
        super("Put bluetooth device name that connect with", new MyPacketFactory());
    }
}
```

#### Sending packet
```java
MyPacket packet = new MyPacket();
/* Set packet data here */
mService.sendPacket(new PacketWrapper(packet));
```
#### Receiving packet
```java
IPassiveGeppaServiceListener listener = new IPassiveGeppaServiceListener.Stub() {
    public void onConnectionStateChanged(in ConnectionState state) {
        // Handle connection event here
    }
    public void onReceivePacket(in PacketWrapper packet) {
        // Handle received packet here
    }
}
mService.registerGeppaServiceListener(listener);
```


### Getting Started
Add following lines to build.gradle
```grooby
repositories {
    maven {
        url "http://dl.bintray.com/cattaka/maven"
    }
}
dependencies {
    compile('net.cattaka:libgeppa:1.0.0')
}
```
