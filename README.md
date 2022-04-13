# Protection SDK     
### 概述       
Protection SDK保障app的稳定运行，包含系统崩溃、系统优化、安全模式等兜底任务。   

主要可以分成两个部分：  
- CrashKiller模块：拦截崩溃，保障app在使用过程中不发生crash
- SafeMode模块：当用户连续冷启动发生crash时，进入安全模式，尝试解决崩溃问题，改善用户体验

