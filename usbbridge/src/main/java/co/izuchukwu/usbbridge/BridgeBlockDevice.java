package co.izuchukwu.usbbridge;

import com.github.mjdev.libaums.driver.BlockDeviceDriver;
import com.github.mjdev.libaums.partition.PartitionTableEntry;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.waldheinz.fs.BlockDevice;
import de.waldheinz.fs.ReadOnlyException;

public class BridgeBlockDevice implements BlockDevice {

    private BlockDeviceDriver blockDevice;
    private com.github.mjdev.libaums.partition.PartitionTableEntry partitionTableEntry;

    public BridgeBlockDevice(BlockDeviceDriver blockDevice, PartitionTableEntry partitionTableEntry) {
        this.blockDevice = blockDevice;
        this.partitionTableEntry = partitionTableEntry;
    }

    @Override
    public long getSize() throws IOException {
        return partitionTableEntry.getTotalNumberOfSectors() * blockDevice.getBlockSize();
    }

    @Override
    public void read(long devOffset, ByteBuffer dest) throws IOException {
        blockDevice.read(devOffset, dest);
    }

    @Override
    public void write(long devOffset, ByteBuffer src) throws ReadOnlyException, IOException, IllegalArgumentException {
        blockDevice.write(devOffset, src);
    }

    @Override
    public void flush() throws IOException {
        // ¯\_(ツ)_/¯
    }

    @Override
    public int getSectorSize() throws IOException {
        return blockDevice.getBlockSize();
    }

    @Override
    public void close() throws IOException {
        return;
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }
}
