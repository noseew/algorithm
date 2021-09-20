# spring 实现 全局请求响应 加解密


**ControllerAdvice**是springmvc controller增强器

```
ControllerAdvice三个用处：
1. ModelAttribute: 暴露@RequestMapping 方法返回值为模型数据：放在功能处理方法的返回值上时，是暴露功能处理方法的返回值为模型数据，用于视图页面展示时使用。
2. InitBinder : 用于自定义@RequestMapping 方法参数绑定
3. ResponseBodyAdvice : 用于@ResponseBody返回值增加处理
ControllerAdvice初始化：
Spring mvc 启动时调用RequestMappingHandlerAdapter类的initControllerAdviceCache()方法进行初始化

```


```
ResponseBodyAdvice 和 RequestBodyAdvice
ResponseBodyAdvice是spring4.1的新特性，其作用是在响应体写出之前做一些处理；比如，修改返回值、加密等。

```
