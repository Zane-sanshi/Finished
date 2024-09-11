<template>
  <div>
    <!--页面区域-->
    <div class="page-view">
      <div class="table-operations">
        <a-space>
          <a-button type="primary">新增</a-button>
          <a-button>批量删除</a-button>
          <a-input-search addon-before="名称" enter-button />
        </a-space>
      </div>
      <a-table
        size="middle"
        rowKey="id"
        :loading="data.loading"
        :columns="columns"
        :data-source="data.dataList"
        :scroll="{ x: 'max-content' }"
        :row-selection="rowSelection"
        :pagination="{
          size: 'default',
          current: data.page,
          pageSize: data.pageSize,
          onChange: (current) => (data.page = current),
          showSizeChanger: false,
          showTotal: (total) => `共${total}条数据`,
        }"
      >
        <template #bodyCell="{ text, record, index, column }">
          <template v-if="column.key === 'operation'">
            <span>
              <a @click="handleEdit(record)">编辑</a>
              <a-divider type="vertical" />
              <a-popconfirm
                title="确定删除?"
                ok-text="是"
                cancel-text="否"
                @confirm="confirmDelete(record)"
              >
                <a href="#">删除</a>
              </a-popconfirm>
            </span>
          </template>
          <template v-else-if="column.key !== 'index' && column.key !== 'username'">
            <span :class="getTagClass(text)">{{ getTagText(text) }}</span>
          </template>
        </template>
      </a-table>
    </div>
  </div>
</template>
   
<script setup lang="ts">
import { FormInstance, message } from 'ant-design-vue';
import { listApi, deleteApi } from '/@/api/skills';
import { BASE_URL } from '/@/store/constants';
   
const columns = reactive([
  {
    title: '序号',
    dataIndex: 'index',
    key: 'index',
    width: 60,
  },
  {
    title: '用户名',
    dataIndex: 'username',
    key: 'username',
    width: 160,
  },
  {
    title: '专业技术',
    children: [
      {
        title: '技术能力',
        dataIndex: 'technicalSkills',
        key: 'technicalSkills',
        width: 120,
      },
      {
        title: '证书资质',
        dataIndex: 'certifications',
        key: 'certifications',
        width: 120,
      },
    ],
  },
  {
    title: '软技能',
    children: [
      {
        title: '沟通能力',
        dataIndex: 'communication',
        key: 'communication',
        width: 120,
      },
      {
        title: '团队合作',
        dataIndex: 'teamwork',
        key: 'teamwork',
        width: 120,
      },
      {
        title: '问题解决',
        dataIndex: 'problemSolving',
        key: 'problemSolving',
        width: 120,
      },
      {
        title: '领导力',
        dataIndex: 'leadership',
        key: 'leadership',
        width: 120,
      },
    ],
  },
  {
    title: '学习能力',
    children: [
      {
        title: '持续学习',
        dataIndex: 'continuousLearning',
        key: 'continuousLearning',
        width: 120,
      },
      {
        title: '适应能力',
        dataIndex: 'adaptability',
        key: 'adaptability',
        width: 120,
      },
    ],
  },
  {
    title: '工作经验',
    children: [
      {
        title: '相关经验',
        dataIndex: 'relevantExperience',
        key:'relevantExperience',
        width: 120,
      },
      {
        title:'成就展示',
        dataIndex: 'achievements',
        key: 'achievements',
        width: 120,
      },
    ],
  },
  {
    title: '个人特质',
    children: [
      {
        title: '责任心',
        dataIndex: 'responsibility',
        key: 'responsibility',
        width: 120,
      },
      {
        title: '抗压能力',
        dataIndex: 'stressManagement',
        key:'stressManagement',
        width: 120,
      },
      {
        title: '诚信正直',
        dataIndex: 'integrity',
        key: 'integrity',
        width: 120,
      },
    ],
  },
  {
    title: '行业知识',
    children: [
      {
        title: '行业洞察',
        dataIndex: 'industryInsights',
        key: 'industryInsights',
        width: 120,
      },
      {
        title: '法规政策',
        dataIndex: 'regulatoryKnowledge',
        key:'regulatoryKnowledge',
        width: 120,
      },
    ],
  },
  {
    title: '操作',
    dataIndex: 'action',
    key: 'operation',
    align: 'center',
    fixed: 'right',
    width: 140,
  },
]);
   
// 页面数据
const data = reactive({
  dataList: [],
  loading: false,
  currentAdminUserName: '',
  keyword: '',
  selectedRowKeys: [] as any[],
  pageSize: 10,
  page: 1,
});
   
const myform = ref<FormInstance>();
   
onMounted(() => {
  getDataList();
});
   
const getDataList = () => {
  data.loading = true;
  listApi({
    keyword: data.keyword,
  })
    .then((res) => {
      data.loading = false;
      console.log(res);
      res.data.forEach((item: any, index: any) => {
        item.index = index + 1;
      });
      data.dataList = res.data;
    })
    .catch((err) => {
      data.loading = false;
      console.log(err);
    });
};

interface DataItem {
    id: number;
}
   
const rowSelection = ref({
  onChange: (selectedRowKeys: (string | number)[], selectedRows: DataItem[]) => {
    console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
    data.selectedRowKeys = selectedRowKeys;
  },
});
  
const confirmDelete = (record: any) => {
  console.log('delete', record);
  deleteApi({ ids: record.id })
      .then((res) => {
        getDataList();
      })
      .catch((err) => {
        message.error(err.msg || '操作失败');
      });
};
  
const handleEdit = (record: any) => {
    
};
   
const getTagClass = (score: number) => {
  if (score >= 8) return 'tag-success';
  if (score >= 6) return 'tag-info';
  if (score >= 4) return 'tag-warning';
  return 'tag-danger';
};
   
const getTagText = (score: number) => {
  if (score >= 8) return '优';
  if (score >= 6) return '良';
  if (score >= 4) return '中';
  return '差';
};
</script>
  
<style scoped lang="less">
.page-view {
  min-height: 100%;
  background: #fff;
  padding: 24px;
  display: flex;
  flex-direction: column;
}
  
.table-operations {
  margin-bottom: 16px;
  text-align: right;
}
  
.table-operations > button {
  margin-right: 8px;
}
  
.tag-success {
  background-color: #F0F9EB;
  color: #52c41a;
  padding: 4px 8px;
  border-radius: 4px;
}
  
.tag-info {
  background-color: #ECF5FF;
  color: #1890ff;
  padding: 4px 8px;
  border-radius: 4px;
}
  
.tag-warning {
  background-color: #FDF6EC;
  color: #faad14;
  padding: 4px 8px;
  border-radius: 4px;
}
  
.tag-danger {
  background-color: #FEF0F0;
  color: #ff4d4f;
  padding: 4px 8px;
  border-radius: 4px;
}
  
.table-operations {
  margin-bottom: 16px;
  text-align: right;
}
  
.table-operations > button {
  margin-right: 8px;
}
</style>