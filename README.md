# BS 商品比价网站

本仓库是BS商品比价网站项目的前后端合并后添加了docker配置的网站，只记录了开始配置Docker以及之后的流程。

具体的前后端开发流程可以通过访问原本的开发仓库进行查看。

[前端仓库](https://github.com/NoneZJL/BS-front)

[后端仓库](https://github.com/NoneZJL/BS-back)

## 代码运行方式

1. 首先克隆仓库内容到本地

```bash
git clone https://github.com/NoneZJL/BS-PriceCompare.git
```

2. 然后进入刚刚克隆的页面并且使用docker-compose运行代码

```bash
# 进入仓库
cd BS-PriceCompare

# 运行代码
docker-compose up
```

> 注意：如果在运行docker命令时出错，建议可以现在本地拉取所需要的镜像文件，然后再重新运行docker命令
>
> 需要拉取的镜像文件包含这些
>
> 1. mysql:8.0
> 2. node:latest
> 3. nginx:alpine
> 4. openjdk:17-jdk-slim
>
> 这些镜像的拉取命令可以统一使用如下命令完成
>
> $ docker pull [上面这些东西]
>
> 并且此时建议使用无缓存的方式重新构建镜像并且启动
> 

```bash
# 镜像构建
$ docker-compose build --no-cache
# 镜像启动
$ docker-compose up
```

3. 此时就可以打开浏览器并在本地的5173端口访问网站内容
4. 当项目运行结束后，也可以使用docker命令来删除容器

```bash
docker-compose down
```

> 注意：首次获取商品信息的时候可能会有获取失败的情况，这是因为docker内部的浏览器还没有正常打开，是正常情况，一般第二次获取商品信息的时候就不会出现问题。
> 
> 同时，目前京东可能出现无法获取商品信息的情况，这是因为京东最近更新了反爬虫的机制，导致现在几乎无法通过Java爬虫的方式获取京东商品信息。但是苏宁以及小米有品两个网站的信息应该都是能够正常获取的。