package co.izuchukwu.usbbridge;

import com.github.mjdev.libaums.driver.BlockDeviceDriver;
import com.github.mjdev.libaums.partition.PartitionTableEntry;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.waldheinz.fs.BlockDevice;
import de.waldheinz.fs.FileSystem;
import de.waldheinz.fs.FileSystemFactory;
import de.waldheinz.fs.ReadOnlyException;

public class BridgeBlockDevice implements BlockDevice {

    private BlockDeviceDriver blockDevice;
    private com.github.mjdev.libaums.partition.PartitionTableEntry partitionTableEntry;
    public FileSystem fs;

    public BridgeBlockDevice(BlockDeviceDriver blockDevice, PartitionTableEntry partitionTableEntry) throws IOException {
        this.blockDevice = blockDevice;
        this.partitionTableEntry = partitionTableEntry;
        this.fs = FileSystemFactory.create(this, true);
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
        fs.flush();
    }

    @Override
    public int getSectorSize() throws IOException {
        return blockDevice.getBlockSize();
    }

    @Override
    public void close() throws IOException {
        fs.close();
    }

    @Override
    public boolean isClosed() {
        return fs.isClosed();
    }

    @Override
    public boolean isReadOnly() {
        return fs.isReadOnly();
    }
}
