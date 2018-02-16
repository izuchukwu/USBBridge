package co.izuchukwu.usbbridge;

import android.util.Log;

import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;

import java.io.IOException;

import de.waldheinz.fs.BlockDevice;
import de.waldheinz.fs.fat.FatFileSystem;

/**
 * Created by izuchukwuelechi on 2/16/18.
 */

public class BridgeFileSystem implements FileSystem {

    private FatFileSystem fs;
    private BlockDevice blockDevice;

    public BridgeFileSystem(FatFileSystem fs, BlockDevice blockDevice) {
        this.fs = fs;
    }

    @Override
    public UsbFile getRootDirectory() {
        return null;
    }

    @Override
    public String getVolumeLabel() {
        return fs.getVolumeLabel();
    }

    @Override
    public long getCapacity() {
        return fs.getTotalSpace();
    }

    @Override
    public long getOccupiedSpace() {
        return fs.getTotalSpace() - fs.getFreeSpace();
    }

    @Override
    public long getFreeSpace() {
        fs.getFreeSpace();
    }

    @Override
    public int getChunkSize() {
        // https://goo.gl/P7dHqp -> this is probably also wrong
        try {
            blockDevice.getSectorSize();
        } catch (IOException e) {
            Log.e("[USBBridge]", "Failed to get chunk size for BridgeFileSystem", e);
        }
    }
}
