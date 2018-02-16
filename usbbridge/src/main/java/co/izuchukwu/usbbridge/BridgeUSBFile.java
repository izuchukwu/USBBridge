package co.izuchukwu.usbbridge;

import android.util.Log;

import com.github.mjdev.libaums.fs.AbstractUsbFile;
import com.github.mjdev.libaums.fs.UsbFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.waldheinz.fs.FsDirectory;
import de.waldheinz.fs.FsDirectoryEntry;
import de.waldheinz.fs.FsFile;

/**
 * Created by izuchukwuelechi on 2/16/18.
 */

public class BridgeUSBFile extends AbstractUsbFile {

    private FsDirectoryEntry entry;
    private FsDirectory directory;
    private FsFile file;

    public BridgeUSBFile(FsDirectoryEntry entry) throws IOException {
        this.entry = entry;

        if(entry.isDirectory()) {
            directory = entry.getDirectory();
        } else {
            file = entry.getFile();
        }
    }

    public BridgeUSBFile(FsDirectory directory) {
        this.directory = directory;
    }

    @Override
    public boolean isDirectory() {
        return directory != null;
    }

    @Override
    public String getName() {
        return entry.getName();
    }

    @Override
    public void setName(String newName) throws IOException {
        entry.setName(newName);
    }

    @Override
    public long createdAt() {
        try {
            return entry.getCreated();
        } catch (IOException e) {
            Log.e("[USBBridge]", "Error getting file's creation date.", e);
        }
    }

    @Override
    public long lastModified() {
        try {
            return entry.getLastModified();
        } catch (IOException e) {
            Log.e("[USBBridge]", "Error getting file's last modified date.", e);
        }
    }

    @Override
    public long lastAccessed() {
        try {
            return entry.getLastAccessed();
        } catch (IOException e) {
            Log.e("[USBBridge]", "Error getting file's last access date.", e);
        }
    }

    @Override
    public UsbFile getParent() {
        // Deprecated in fat32-lib: https://goo.gl/wvXgNC
        // Unsupported in libaums-javafs too: https://goo.gl/xpdtc3
        // So we won't offer it here
        Log.e("[USBBridge]", "Getting file parent is unsupported. See https://goo.gl/wvXgNC");
        return null;
    }

    @Override
    public String[] list() throws IOException {
        if (directory == null) {
            Log.e("[USBBridge]", "Can't list contents of a file ¯\\_(ツ)_/¯ ");
            return null;
        }

        List<String> list = new ArrayList<>();
        Iterator<FsDirectoryEntry> iterator = directory.iterator();
        while(iterator.hasNext()) {
            FsDirectoryEntry entry = iterator.next();
            list.add(entry.getName());
        }

        String[] array = new String[list.size()];
        return list.toArray(array);
    }

    @Override
    public UsbFile[] listFiles() throws IOException {
        if (directory == null) {
            Log.e("[USBBridge]", "Can't list contents of a file ¯\\_(ツ)_/¯");
            return null;
        }

        List<UsbFile> list = new ArrayList<>();
        Iterator<FsDirectoryEntry> iterator = directory.iterator();
        while(iterator.hasNext()) {
            FsDirectoryEntry entry = iterator.next();
            list.add(new BridgeUSBFile(entry));
        }

        UsbFile[] array = new UsbFile[list.size()];
        return list.toArray(array);
    }

    @Override
    public long getLength() {
        if (directory != null) {
            Log.e("[USBBridge]", "Can't get the length of a directory.");
            return 0;
        }

        return file.getLength();
    }

    @Override
    public void setLength(long newLength) throws IOException {
        if (directory != null) {
            Log.e("[USBBridge]", "Can't set the length of a directory.");
            return;
        }

        file.setLength(newLength);
    }

    @Override
    public void read(long offset, ByteBuffer destination) throws IOException {
        if (directory != null) {
            Log.e("[USBBridge]", "Can't read from a directory.");
            return;
        }

        file.read(offset, destination);
    }

    @Override
    public void write(long offset, ByteBuffer source) throws IOException {
        if (directory != null) {
            Log.e("[USBBridge]", "Can't write to a directory.");
            return;
        }

        file.write(offset, source);
    }

    @Override
    public void flush() throws IOException {
        if (directory != null) {
            Log.e("[USBBridge]", "Can't flush a directory.");
            return;
        }

        file.flush();
    }

    @Override
    public void close() throws IOException {
        flush();
    }

    @Override
    public UsbFile createDirectory(String name) throws IOException {
        if (directory == null) {
            Log.e("[USBBridge]", "Can't make a directory out of a file ¯\\_(ツ)_/¯");
            return null;
        }

        return new BridgeUSBFile(directory.addDirectory(name));
    }

    @Override
    public UsbFile createFile(String name) throws IOException {
        if (directory != null) {
            Log.e("[USBBridge]", "Can't make a file out of a directory ¯\\_(ツ)_/¯");
            return null;
        }

        return new BridgeUSBFile(directory.addFile(name));
    }

    @Override
    public void moveTo(UsbFile destination) throws IOException {
        // Implement me: https://goo.gl/uuyyLb
        Log.v("[USBBridge]", "Nice try, but moving files is unsupported");
    }

    @Override
    public void delete() throws IOException {
        entry.getParent().remove(entry.getName());
    }

    @Override
    public boolean isRoot() {
        return false //figure this out;
    }
}
