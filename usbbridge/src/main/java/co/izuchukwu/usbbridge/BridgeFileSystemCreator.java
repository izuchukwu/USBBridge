package co.izuchukwu.usbbridge;

import android.util.Log;

import com.github.mjdev.libaums.driver.BlockDeviceDriver;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.FileSystemCreator;
import com.github.mjdev.libaums.partition.PartitionTableEntry;

import java.io.IOException;
import java.nio.ByteBuffer;

public class BridgeFileSystemCreator implements FileSystemCreator {
    public static String hello(){
        return "Hi!";
    }

    @Override
    public FileSystem read(PartitionTableEntry entry, BlockDeviceDriver blockDevice) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        blockDevice.read(0, buffer);

        try {
            BridgeBlockDevice bridgeBlockDevice = new BridgeBlockDevice(blockDevice, entry);

        } catch (IOException e) {
            Log.e("[USBBridge]", "Error modeling the file system. Are you sure this is FAT?");
        }

        return null;
    }
}
