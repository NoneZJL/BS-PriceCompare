# 使用官方的 Node.js 镜像作为基础镜像
FROM node:latest as build

# 设置工作目录
WORKDIR /app

# 复制 package.json 和 pnpm-lock.yaml
COPY package.json pnpm-lock.yaml ./

# 安装依赖
RUN npm install -g pnpm && pnpm install

# 复制项目文件
COPY . .

# 构建项目
RUN pnpm run build

# 使用 Nginx 作为生产环境服务器
FROM nginx:alpine

# 复制构建后的文件到 Nginx 的默认目录
COPY --from=build /app/dist /usr/share/nginx/html

# 暴露端口
EXPOSE 80

# 启动 Nginx
CMD ["nginx", "-g", "daemon off;"]