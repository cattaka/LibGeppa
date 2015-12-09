
### ActiveGeppaService
Provide manual connect functions.
Supports: USB-Host-API, Bluetooth, TCP/IP

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

### PassiveGeppaService
#### AdkPassiveGeppaService
Provide auto connect functions for ADK.

Supports: ADK
```java
public class GeppaServiceEx extends AdkPassiveGeppaService<MyPacket> {
    public GeppaServiceEx() {
        super(new MyPacketFactory());
    }
}
```

#### BluetoothPassiveGeppaService
Provide auto connect functions for Bluetooth.

Supports: Bluetooth(Auto connect)
```java
public class GeppaServiceEx extends GeppaService<MyPacket> {
    public GeppaServiceEx() {
        super("GeppaSample", new MyPacketFactory());
    }
}
```
