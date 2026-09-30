# 猫眼电影购票系统

基于 Spring Boot + Thymeleaf + MySQL 的电影购票系统。

## 技术栈

- Spring Boot 3.2.5
- Spring Data JPA
- Thymeleaf
- MySQL 8.x
- Maven
- JDK 21

## 功能

- 电影列表（正在热映 / 即将上映）
- 电影详情
- 选影院、选场次
- 选座（座位图可视化）
- 锁座下单（10 分钟超时自动释放）
- 模拟支付
- 生成取票码
- 订单列表 / 票券查看

## 快速开始

### 1. 建数据库

\`\`\`sql
CREATE DATABASE movie_ticketing DEFAULT CHARACTER SET utf8mb4;
\`\`\`

### 2. 修改配置

编辑 `src/main/resources/application.properties` 中的数据库账号密码。

### 3. 启动

\`\`\`bash
mvn spring-boot:run
\`\`\`

### 4. 访问

浏览器打开 `http://localhost:8080/`

## 项目结构

\`\`\`
src/main/java/com/maoyan/
├── common/             公共类（统一响应、异常处理）
├── config/             配置
├── controller/         控制器
├── dto/                数据传输对象
├── entity/             JPA 实体
├── repository/         数据访问层
└── service/            业务逻辑层
\`\`\`

## License

MIT
\`\`\`

然后提交：

```bash
git add README.md
git commit -m "docs: 添加项目 README"
git push