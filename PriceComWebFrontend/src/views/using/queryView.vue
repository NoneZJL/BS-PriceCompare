<template>
  <div class="common-layout">
    <el-container>
      <el-header class="header">
        <div class="logo-container">
          <img src="../../assets/logo.png" style="width: 50px" alt="logo" />
        </div>
        <div class="center-text">
          <span class="price-compare">Price Compare</span>
        </div>
        <div class="toolbar">
          <el-dropdown>
            <el-icon style="margin-right: 10px; margin-top: 1px; color: white">
              <User />
            </el-icon>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="jumpHome">个人主页</el-dropdown-item>
                <el-dropdown-item @click="jumpHistory">历史记录</el-dropdown-item>
                <el-dropdown-item @click="jumpRemainder">我的收藏</el-dropdown-item>
                <el-dropdown-item @click="confirmLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <span>{{ username }}</span>
        </div>
      </el-header>
      <el-main>
        <div class="search-container">
          <div class="search">
            <el-input v-model="search" placeholder="请输入您想要搜索的商品">
              <template #suffix>
                <el-icon class="el-input__icon" @click="handleSearch">
                  <Search />
                </el-icon>
              </template>
            </el-input>
          </div>
        </div>
        <div class="now-searching-container">
          <span v-if="nowSearching">当前查询的内容为：{{ nowSearching }}</span>
          <span v-else>当前暂未进行查询</span>
        </div>
        <div class="search-website">
          <el-tabs v-model="activeWebsite" class="demo-tabs" @tab-click="handleClick">
            <el-tab-pane name="jd">
              <template #label>
                <el-icon><Shop /></el-icon>
                <span style="margin-left: 5px">京东</span>
              </template>
            </el-tab-pane>
            <el-tab-pane name="wph">
              <template #label>
                <el-icon><Shop /></el-icon>
                <span style="margin-left: 5px">小米有品</span>
              </template>
            </el-tab-pane>
            <el-tab-pane name="sn">
              <template #label>
                <el-icon><Shop /></el-icon>
                <span style="margin-left: 5px">苏宁</span>
              </template>
            </el-tab-pane>
          </el-tabs>
        </div>
        <div class="search-content" v-if="activeWebsite !== 'priceHistory'">
          <el-row :gutter="20">
            <el-col
              :xs="24"
              :sm="12"
              :md="8"
              :lg="6"
              :xl="4"
              v-for="(item, index) in currentProducts"
              :key="index"
            >
              <el-card :body-style="{ padding: '0px' }" class="product-card">
                <img
                  :src="item.img || defaultImage"
                  class="image"
                  @click="jumpUrl(item.detailUrl)"
                />
                <div style="padding: 14px">
                  <span class="description">{{ item.description }}</span>
                  <div class="bottom">
                    <span class="price">¥{{ item.price }}</span>
                    <el-button
                      type="text"
                      class="button"
                      @click="jumpDetail(item.detailUrl, item.description, item.img, item.price)"
                      >商品详情</el-button
                    >
                    <el-button
                      type="text"
                      class="button"
                      @click="setRemainder(item.description, item.price, item.img, item.detailUrl)"
                      >添加到收藏</el-button
                    >
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
          <el-empty v-if="currentProducts.length === 0" description="无搜索结果" />
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import router from '@/router/index'
import { useUserStore, useQueryStore, useDetailStore } from '@/stores/index'
import { queryGoodService, insertRemainderService } from '@/api/query'
import { jdGetGoodsBySearchingNameService } from '@/api/jd'
import { snGetGoodsBySearchingNameService } from '@/api/sn'
import { wphGetGoodsBySearchingNameService } from '@/api/wph'

const username = ref('username')
const search = ref('')
const nowSearching = ref('')
const userStore = useUserStore()
const queryStore = useQueryStore()
const detailStore = useDetailStore()
const activeWebsite = ref('jd')
const defaultImage = 'https://via.placeholder.com/150'

onMounted(async () => {
  if (userStore.token === '') {
    ElMessage.error('请先登录')
    router.push('/login')
  }
  username.value = userStore.username
  if (queryStore.toRefresh === false) {
    nowSearching.value = queryStore.queryingName
    jdProducts.value = queryStore.jdProducts
    snProducts.value = queryStore.snProducts
    wphProducts.value = queryStore.wphProducts
    activeWebsite.value = queryStore.website
    return
  }
  if (!queryStore.isQueryingNameEmpty()) {
    nowSearching.value = queryStore.queryingName
    queryStore.removeQueringName()
    const loading = ElLoading.service({
      lock: true,
      text: '搜索中',
      background: 'rgba(0, 0, 0, 0.7)'
    })
    // JD
    const JDanswer = await jdGetGoodsBySearchingNameService(nowSearching.value)
    if (JDanswer.data.code === 2) {
      router.push('/login')
      return
    }
    if (JDanswer.data.code === 1) {
      ElMessage.error(JDanswer.data.err)
      loading.close()
      return
    }
    const JDgoodList = JDanswer.data.payload
    jdProducts.value = JDgoodList.map((item) => item)
    queryStore.setJdProducts(jdProducts.value)
    // loading.close()
    // SN
    const SNanswer = await snGetGoodsBySearchingNameService(nowSearching.value)
    if (SNanswer.data.code === 2) {
      router.push('/login')
      return
    }
    if (SNanswer.data.code === 1) {
      ElMessage.error(SNanswer.data.err)
      loading.close()
      return
    }
    const SNgoodList = SNanswer.data.payload
    snProducts.value = SNgoodList.map((item) => item)
    queryStore.setSnProducts(snProducts.value)
    // WPH
    const WPHanswer = await wphGetGoodsBySearchingNameService(nowSearching.value)
    if (WPHanswer.data.code === 2) {
      router.push('/login')
      return
    }
    if (WPHanswer.data.code === 1) {
      ElMessage.error(WPHanswer.data.err)
      loading.close()
      return
    }
    const WPHgoodList = WPHanswer.data.payload
    wphProducts.value = WPHgoodList.map((item) => item)
    queryStore.setWphProducts(wphProducts.value)
    loading.close()
  }
  activeWebsite.value = 'jd'
  queryStore.resetRefresh()
})

const jdProducts = ref([])
const wphProducts = ref([])
const snProducts = ref([])

const currentProducts = computed(() => {
  switch (activeWebsite.value) {
    case 'jd':
      return jdProducts.value
    case 'wph':
      return wphProducts.value
    case 'sn':
      return snProducts.value
    default:
      return []
  }
})

const jumpHome = () => {
  router.push('/home')
}

const jumpHistory = () => {
  router.push('/history')
}

const jumpRemainder = () => {
  router.push('/remainder')
}

const jumpUrl = (url) => {
  window.open(url, '_blank')
}

const confirmLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      userStore.removeToken()
      userStore.removeUsername()
      queryStore.removeQueringName()
      queryStore.removeJdProducts()
      queryStore.removeSnProducts()
      queryStore.removeWphProducts()
      queryStore.removeWebsite()
      logout()
    })
    .catch(() => {})
}

const setRemainder = (description, price, img, detailUrl) => {
  ElMessageBox.confirm('确定要添加到收藏吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      const res = await insertRemainderService(
        description,
        price,
        img,
        detailUrl,
        activeWebsite.value
      )
      if (res.data.code === 2) {
        router.push('/login')
        return
      }
      if (res.data.code === 1) {
        ElMessage.error(res.data.err)
        return
      }
      ElMessage.success('成功添加到收藏')
    })
    .catch(() => {})
}

const logout = () => {
  router.push('/login')
}

const handleSearch = async () => {
  // 去除输入内容两侧的空格
  const trimmedSearch = search.value.trim()
  // 判断输入内容是否为空
  if (!trimmedSearch) {
    ElMessage.error('搜索内容不能为空')
    return
  }
  queryStore.setQueryingName(trimmedSearch)
  const loading = ElLoading.service({
    lock: true,
    text: '搜索中',
    background: 'rgba(0, 0, 0, 0.7)'
  })
  const res = await queryGoodService(trimmedSearch)
  if (res.data.code === 2) {
    router.push('/login')
    return
  }
  if (res.data.code === 1) {
    ElMessage.error(res.data.err)
    return
  }
  nowSearching.value = trimmedSearch

  // JD
  const JDanswer = await jdGetGoodsBySearchingNameService(nowSearching.value)
  console.log(JDanswer)
  if (JDanswer.data.code === 2) {
    router.push('/login')
    return
  }
  if (JDanswer.data.code === 1) {
    ElMessage.error(JDanswer.data.err)
    loading.close()
    return
  }
  const JDgoodList = JDanswer.data.payload
  jdProducts.value = JDgoodList.map((item) => item)
  queryStore.setJdProducts(jdProducts.value)
  // loading.close()
  // SN
  const SNanswer = await snGetGoodsBySearchingNameService(nowSearching.value)
  console.log(SNanswer)
  if (SNanswer.data.code === 2) {
    router.push('/login')
    return
  }
  if (SNanswer.data.code === 1) {
    ElMessage.error(SNanswer.data.err)
    loading.close()
    return
  }
  const SNgoodList = SNanswer.data.payload
  snProducts.value = SNgoodList.map((item) => item)
  queryStore.setSnProducts(snProducts.value)
  // WPH
  const WPHanswer = await wphGetGoodsBySearchingNameService(nowSearching.value)
  console.log(WPHanswer)
  if (WPHanswer.data.code === 2) {
    router.push('/login')
    return
  }
  if (WPHanswer.data.code === 1) {
    console.log(WPHanswer)
    ElMessage.error(WPHanswer.data.err)
    loading.close()
    return
  }
  const WPHgoodList = WPHanswer.data.payload
  wphProducts.value = WPHgoodList.map((item) => item)
  queryStore.setWphProducts(wphProducts.value)

  loading.close()
  activeWebsite.value = 'jd'
}

const jumpDetail = (url, description, image, price) => {
  queryStore.setWebsite(activeWebsite.value)
  detailStore.setDetailGoodName(url)
  detailStore.setDetailGoodFrom(activeWebsite.value)
  detailStore.setDescription(description)
  detailStore.setImage(image)
  detailStore.setPrice(price)
  router.push('/detail')
}
</script>

<style scoped>
.now-searching-container {
  text-align: center;
  font-size: 16px;
  margin-top: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: rgb(37, 33, 33);
  color: white;
  font-size: 12px;
  padding: 0 20px;
}

.logo-container {
  display: flex;
  align-items: center;
}

.center-text {
  flex-grow: 1;
  text-align: center;
}

.price-compare {
  font-size: 24px;
  font-weight: bold;
  background: linear-gradient(45deg, #f06, #f90, #ff0, #0f0, #0ff, #00f, #90f, #f06);
  background-size: 400% 400%;
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  animation: gradient 15s ease infinite;
  font-family: 'Pacifico', cursive;
}

@keyframes gradient {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

.toolbar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
}

.search-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.search {
  width: 50vw;
}

.el-input__icon {
  cursor: pointer;
}

.search-website {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.demo-tabs {
  width: 50%;
}

.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}

.product-card {
  width: 100%;
  margin-bottom: 20px;
}

.image {
  width: 100%;
  display: block;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  font-size: 18px;
  color: #f56c6c;
}

.button {
  padding: 0;
  min-height: auto;
}

.detail-info {
  color: #409eff;
  font-size: 14px;
  font-family: Arial, Helvetica, sans-serif;
}

.content {
  margin-top: 10px;
  font-size: 14px;
  color: #606266;
}

.description {
  margin-top: 10px;
  font-size: 14px;
  color: #606266;
  flex-grow: 1; /* 让内容区域占据剩余空间 */
  overflow: hidden; /* 隐藏超出部分 */
  text-overflow: ellipsis; /* 显示省略号 */
  display: -webkit-box;
  -webkit-line-clamp: 3; /* 最多显示3行 */
  -webkit-box-orient: vertical;
  height: 60px; /* 设置固定高度 */
}
</style>
