# 202408_springboot_homework
### 專案 & RESTful API 設計原則說明
本專案提供檢視使用者與相關資產的功能，主要包括：
* 新使用者註冊

  > HTTP method：POST

  > url：/users/register
* 查詢使用者資訊

  > HTTP method：GET
  
  > url：/users/{userId} 
* 更新使用者資訊

  > HTTP method：PUT

  > url：/users/{userId} 
* 刪除使用者

  > HTTP method：DELETE

  > url：/users/{userId} 
* 新增資產

  > HTTP method：POST

  > url：/users/{userId}/assets
* 查詢資產

  > HTTP method：GET

  > url：/users/{userId}/assets
* 更新資產

  > HTTP method：PUT

  > url：/assets/{assetId}
* 刪除資產

  > HTTP method：DELETE

  > url：/assets/{assetId}

### 如何運行專案
下載專案之後，直接開啟 server 依循 API 說明請求即可。

### 使用技術
* SpringBoot 3.0.0
* Redis
* JDBC
* MySql

