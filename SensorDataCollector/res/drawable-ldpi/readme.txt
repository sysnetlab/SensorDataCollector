Android Asset Studio stopped generate icons for ldpi. Since our mdpi icons are 48 x 48. ldpi should be 32 x 32 icons. However, I was too lazy to produce them manually. Instead, I copied everything from mdpi folder here. 

These icons are necessary for the app to render its interface correctly on ldpi devices; otherwise, it will use very large launch icons, which is the cause of issue #141 (https://github.com/sysnetlab/SensorDataCollector/issues/141)
-- By Gray