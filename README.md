# USBBridge 🌉

USBBridge is a lil layer on top of [libaums](https://github.com/magnusja/libaums) that replaces its built-in filesystem implementation with [fat32-lib](https://github.com/waldheinz/fat32-lib)'s, bringing FAT12 and FAT16 support.

### What?
A lot of Android devices support USB OTG, which lets you read USB drives. But most, like Google's Pixel and Nexus devices, don't support FAT12 and FAT16. You can use libaums, fat32-lib, and this bridge between the two to read FAT12 and FAT16 drives on devices that don't support them, all with libaums' nice API.

### Why?
I need FAT16 support to build [TeenageKit](https://github.com/izuchukwu/teenagekit). libaums has it via [javafs](https://github.com/magnusja/libaums/tree/develop/javafs), a JNode FS implementation for libaums by the same dev using the original JNode source. fat32-lib is another JNode FS implementation but with years of fixes added to the original, so let's bridge the two.

### Installing it
Install libaums and fat32-lib with gradle:

```ruby
dependencies {
    implementation 'de.waldheinz:fat32-lib:0.6.5'
    implementation 'com.github.mjdev:libaums:0.5.5'
}
```

Then add the USBBridge java files to your project. I'll get a better install process if this works out.

### Using it
coming sometime

### License?
MIT

---

build bridges y'all, not walls ✨
