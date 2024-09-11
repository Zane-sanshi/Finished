<template>
  <div class="content-list">
    <div class="list-title">我的咨询</div>
    <div class="table-operations">
      <a-space>
        <a-button type="primary" @click="handleAdd">新增咨询</a-button>
      </a-space>
    </div>
    <div class="list-content">
      <div class="order-item-view" v-for="(item, index) in contentData.thingData" :key="index">
        <div class="header flex-view">
          <div class="left">
            <span class="num">{{ item.title }}</span>
          </div>
        </div>
        <div class="bottom flex-view">
          <div class="left">
            <span class="text">{{ item.thingTitle }}</span>
            <span class="open" @click="handleDetail(item)">咨询详情</span>
          </div>
          <div class="right flex-view">
            <span class="text">发布时间</span>
            <span class="money">{{ getFormatTime(item.createTime, true) }}</span>
          </div>
          <span class="right flex-view">
            <a @click="handleEdit(item)">编辑</a>
            <a-divider type="vertical" />
            <a-popconfirm title="确定删除?" ok-text="是" cancel-text="否" @confirm="confirmDelete(item)">
              <a href="#">删除</a>
            </a-popconfirm>
          </span>
        </div>
      </div>
      <template v-if="!postData || postData.length <= 0">
        <a-empty style="width: 100%;margin-top: 200px;" />
      </template>
    </div>
    <!--弹窗区域-->
    <div>
      <a-modal :visible="modal.visile" :forceRender="true" :title="modal.title" width="880px" ok-text="确认"
        cancel-text="取消" @cancel="handleCancel" @ok="handleOk">
        <div>
          <a-form ref="myform" :label-col="{ style: { width: '80px' } }" :model="modal.form" :rules="modal.rules">
            <a-row :gutter="24">
              <a-col span="24">
                <a-form-item label="咨询名称" name="title">
                  <a-input placeholder="请输入" v-model:value="modal.form.title"></a-input>
                </a-form-item>
              </a-col>
              <a-col span="24">
                <a-form-item label="咨询内容">
                  <a-textarea placeholder="请输入" v-model:value="modal.form.description"></a-textarea>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </div> <!-- 表单 -->
      </a-modal>
    </div>
  </div>
</template>
  
<script setup lang="ts">
import { FormInstance, message, SelectProps } from 'ant-design-vue';
import { listApi as listThingList, createApi, updateApi, deleteApi } from '/@/api/thing'
import { getFormatTime } from "/@/utils";
import { useUserStore } from "/@/store";
import { BASE_URL } from "/@/store/constants";
const userStore = useUserStore()
const router = useRouter();

const contentData = reactive({
  selectTagId: -1,
  selectedKeys: [],
  loading: false,

  thingData: [],
  pageData: [],
})

onMounted(() => {
  getThingList({ sort: 'job_guide' })
})

const getThingList = (data) => {
  contentData.loading = true
  listThingList(data).then(res => {
    contentData.loading = false
    res.data.forEach((item, index) => {
      if (item.cover) {
        item.cover = BASE_URL + '/api/staticfiles/image/' + item.cover
      }
    })
    contentData.thingData = res.data
    console.log(contentData.thingData)
  }).catch(err => {
    console.log(err)
    contentData.loading = false
  })
}

const handleDetail = (item) => {
  // 跳转新页面
  let text = router.resolve({ name: 'detail', query: { id: item.id, sort: item.sort } })
  window.open(text.href, '_blank')
}

const modal = reactive({
  visile: false,
  editFlag: false,
  title: '',
  form: {
    id: undefined,
    title: undefined,
    classificationId: undefined,
    description: undefined,
    tag: [],
    status: undefined,
    location: undefined,
    salary: undefined,
    workExpe: undefined,
    education: undefined,
    sort: undefined
  },
  rules: {
    title: [{ required: true, message: '请输入名称', trigger: 'change' }],
  },
});

const myform = ref()

const resetModal = () => {
  myform.value?.resetFields();
};

const confirmDelete = (record: any) => {
  console.log('delete', record);
  deleteApi({ ids: record.id })
    .then((res) => {
      getThingList({ sort: "job_guide" })
    })
    .catch((err) => {
      message.error(err.msg || '操作失败');
    });
};

const handleEdit = (record: any) => {
  resetModal();
  modal.visile = true;
  modal.editFlag = true;
  modal.title = '编辑';
  // 重置
  for (const key in modal.form) {
    modal.form[key] = undefined;
  }
  for (const key in record) {
    if (record[key]) {
      modal.form[key] = record[key]
    }
  }
};

const handleAdd = () => {
  resetModal();
  modal.visile = true;
  modal.editFlag = false;
  modal.title = '新增';
  // 重置
  for (const key in modal.form) {
    modal.form[key] = undefined;
  }
};

const handleOk = () => {
  myform.value
    ?.validate()
    .then(() => {
      const formData = new FormData();
      if (modal.editFlag) {
        formData.append('id', modal.form.id)
      }
      formData.append('title', modal.form.title)

      formData.append('sort', 2);

      let userId = userStore.user_id
      formData.append('userId', userId || '')

      formData.append('description', modal.form.description || '')

      // if (modal.form.status) {
      //   formData.append('status', modal.form.status)
      // }
      formData.append('status', '1')

      if (modal.editFlag) {
        updateApi(formData).then((res) => {
          hideModal()
          getThingList({ sort: "job_guide" })
          message.success('操作成功')
        }).catch((err) => {
          console.log(err)
          message.error(err.msg || '操作失败')
        })
      } else {
        createApi(formData).then((res) => {
          hideModal()
          getThingList({ sort: "job_guide" })
          message.success('操作成功')
        }).catch((err) => {
          console.log(err)
          message.error(err.msg || '操作失败')
        })
      }
    })
    .catch((err) => {
      console.log(err, '不能为空');
    });
};

const handleCancel = () => {
  hideModal();
};

const hideModal = () => {
  modal.visile = false;
};
</script>
<style scoped lang="less">
.flex-view {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
}

.content-list {
  -webkit-box-flex: 1;
  -ms-flex: 1;
  flex: 1;

  .list-title {
    color: #152844;
    font-weight: 600;
    font-size: 18px;
    line-height: 24px;
    height: 24px;
    margin-bottom: 4px;
  }
}

.order-item-view {
  background: #f7f9fb;
  border-radius: 4px;
  padding: 16px;
  margin-top: 12px;

  .header {
    border-bottom: 1px solid #cedce4;
    padding-bottom: 12px;
    -webkit-box-pack: justify;
    -ms-flex-pack: justify;
    justify-content: space-between;
    font-size: 14px;

    .text {
      color: #6f6f6f;
    }

    .mg-4 {
      margin-left: 4px;
    }

    .num {
      font-weight: 500;
      color: #152844;
    }

    .num {
      font-weight: 500;
      color: #152844;
    }

    .time {
      margin-left: 16px;
      color: #a1adc5;
    }

    .state {
      color: #ff7b31;
      font-weight: 600;
      margin-left: 10px;
    }
  }

  .content {
    padding: 12px 0;
    overflow: hidden;

    .left-list {
      overflow: hidden;
      height: 132px;
      -webkit-box-flex: 2;
      -ms-flex: 2;
      flex: 2;
      padding-right: 16px;

      .list-item {
        height: 60px;
        margin-bottom: 12px;
        overflow: hidden;
        cursor: pointer;
      }

      .thing-img {
        width: 48px;
        height: 100%;
        margin-right: 12px;
      }

      .detail {
        -webkit-box-flex: 1;
        -ms-flex: 1;
        flex: 1;
        -webkit-box-orient: vertical;
        -webkit-box-direction: normal;
        -ms-flex-direction: column;
        flex-direction: column;
      }

      .flex-between {
        -webkit-box-pack: justify;
        -ms-flex-pack: justify;
        justify-content: space-between;
      }

      .flex-between {
        -webkit-box-pack: justify;
        -ms-flex-pack: justify;
        justify-content: space-between;
      }

      .flex-top {
        -webkit-box-align: start;
        -ms-flex-align: start;
        align-items: flex-start;
      }

      .name {
        color: #152844;
        font-weight: 600;
        font-size: 14px;
        line-height: 18px;
      }

      .count {
        color: #484848;
        font-size: 12px;
      }

      .flex-between {
        -webkit-box-pack: justify;
        -ms-flex-pack: justify;
        justify-content: space-between;
      }

      .flex-center {
        -webkit-box-align: center;
        -ms-flex-align: center;
        align-items: center;
      }

      .type {
        color: #6f6f6f;
        font-size: 12px;
      }

      .price {
        color: #ff7b31;
        font-weight: 600;
        font-size: 14px;
      }
    }

    .right-info {
      -webkit-box-flex: 1;
      -ms-flex: 1;
      flex: 1;
      border-left: 1px solid #cedce4;
      padding-left: 12px;
      line-height: 22px;
      font-size: 14px;

      .title {
        color: #6f6f6f;
      }

      .name {
        color: #152844;
      }

      .text {
        color: #484848;
      }

      .mg {
        margin-bottom: 4px;
      }
    }
  }

  .bottom {
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    -webkit-box-pack: justify;
    -ms-flex-pack: justify;
    justify-content: space-between;
    font-size: 14px;
    padding-top: 14px;

    .text {
      color: #6f6f6f;
    }

    .open {
      color: #4684e2;
      margin-left: 8px;
      cursor: pointer;
    }

    .right {
      -webkit-box-align: center;
      -ms-flex-align: center;
      align-items: center;
    }

    .text {
      color: #6f6f6f;
    }

    .num {
      color: #152844;
      margin: 0 40px 0 8px;
    }

    .money {
      font-size: 14px;
      color: #6f6f6f;
      margin-left: 8px;
    }
  }

}

.order-item-view:first-child {
  margin-top: 16px;
}

.table-operations {
  margin-bottom: 16px;
  text-align: right;
}
</style>
  