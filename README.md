## LibGeppa
LibGeppa is a library to manage connection with HW, such as ADK, USB-Host-API, Bluetooth.
This provides Android Service.
Once service is started, it manages connected/disconnected events.
To send/receive message from activity, you can bind the service and set listeneres.

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


#### Sending packet
```java
MyPacket packet = new MyPacket();
/* Set packet data here */
mService.sendPacket(new PacketWrapper(packet));
```

### BluetoothPassiveGeppaService
This provides auto connect functions for Bluetooth.

Supports: Bluetooth(Auto connect)

#### Creating service
```java
public class GeppaServiceEx extends GeppaService<MyPacket> {
    public GeppaServiceEx() {
        super("GeppaSample", new MyPacketFactory());
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
