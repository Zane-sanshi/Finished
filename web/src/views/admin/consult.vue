<template>
    <div>
        <!--页面区域-->
        <div class="page-view">
            <div class="table-operations">
                <a-space>
                    <a-button type="primary" @click="handleAdd">新增</a-button>
                    <a-button @click="handleBatchDelete">批量删除</a-button>
                    <a-input-search addon-before="名称" enter-button @search="onSearch" @change="onSearchChange" />
                </a-space>
            </div>
            <a-table size="middle" rowKey="id" :loading="data.loading" :columns="columns" :data-source="data.dataList"
                :scroll="{ x: 'max-content' }" :row-selection="rowSelection" :pagination="{
                    size: 'default',
                    current: data.page,
                    pageSize: data.pageSize,
                    onChange: (current) => (data.page = current),
                    showSizeChanger: false,
                    showTotal: (total) => `共${total}条数据`,
                }">
                <template #bodyCell="{ text, record, index, column }">
                    <template v-if="column.key === 'operation'">
                        <span>
                            <a @click="handleEdit(record)">编辑</a>
                            <a-divider type="vertical" />
                            <a-popconfirm title="确定删除?" ok-text="是" cancel-text="否" @confirm="confirmDelete(record)">
                                <a href="#">删除</a>
                            </a-popconfirm>
                        </span>
                    </template>
                </template>
            </a-table>
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
                                    <a-input placeholder="请输入" v-model="modal.form.title"></a-input>
                                </a-form-item>
                            </a-col>
                            <a-col span="24">
                                <a-form-item label="咨询内容">
                                    <a-textarea placeholder="请输入" v-model="modal.form.description"></a-textarea>
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
import { createApi, listApi, updateApi, deleteApi } from '/@/api/thing';
import { listApi as listClassificationApi } from '/@/api/classification'
import { listApi as listTagApi } from '/@/api/tag'
import { BASE_URL } from "/@/store/constants";
import { FileImageOutlined } from '@ant-design/icons-vue';

const columns = reactive([

    {
        title: '序号',
        dataIndex: 'index',
        key: 'index',
        width: 60
    },
    {
        title: '咨询标题',
        dataIndex: 'title',
        key: 'title'
    },
    {
        title: '用户名',
        dataIndex: 'userName',
        key: 'userName'
    },
    {
        title: '摘要',
        dataIndex: 'description',
        key: 'description',
        customRender: ({ text, record, index, column }) => text ? text.substring(0, 10) + '...' : '--'
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
const beforeUpload = (file: File) => {
    // 改文件名
    const fileName = new Date().getTime().toString() + '.' + file.type.substring(6);
    const copyFile = new File([file], fileName);
    console.log(copyFile);
    modal.form.imageFile = copyFile;
    return false;
};

// 文件列表
const fileList = ref<any[]>([]);

// 页面数据
const data = reactive({
    dataList: [],
    loading: false,
    keyword: '',
    selectedRowKeys: [] as any[],
    pageSize: 10,
    page: 1,
});

// 弹窗数据源
const modal = reactive({
    visile: false,
    editFlag: false,
    title: '',
    cData: [],
    tagData: [{}],
    form: {
        id: undefined,
        title: undefined,
        description: undefined,
    },
    rules: {
        title: [{ required: true, message: '请输入名称', trigger: 'change' }]
    },
});

const myform = ref<FormInstance>();

onMounted(() => {
    getDataList();
    getCDataList();
    getTagDataList();
});

const getDataList = () => {
    data.loading = true;
    listApi({
        keyword: data.keyword,
        sort: 'job_guide'
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
}

const getCDataList = () => {
    listClassificationApi({}).then(res => {
        modal.cData = res.data
    })
}
const getTagDataList = () => {
    listTagApi({}).then(res => {
        res.data.forEach((item, index) => {
            item.index = index + 1
        })
        modal.tagData = res.data
    })
}

const onSearchChange = (e: Event) => {
    data.keyword = e?.target?.value;
    console.log(data.keyword);
};

const onSearch = () => {
    getDataList();
};

const rowSelection = ref({
    onChange: (selectedRowKeys: (string | number)[], selectedRows: DataItem[]) => {
        console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
        data.selectedRowKeys = selectedRowKeys;
    },
});

const handleAdd = () => {
    resetModal();
    modal.visile = true;
    modal.editFlag = false;
    modal.title = '新增';
    // 重置
    for (const key in modal.form) {
        modal.form[key] = undefined;
    }
    modal.form.cover = undefined
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
            modal.form[key] = record[key];
        }
    }
    if (modal.form.cover) {
        modal.form.coverUrl = BASE_URL + '/api/staticfiles/image/' + modal.form.cover
        modal.form.cover = undefined
    }
};

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

const handleBatchDelete = () => {
    console.log(data.selectedRowKeys);
    if (data.selectedRowKeys.length <= 0) {
        console.log('hello');
        message.warn('请勾选删除项');
        return;
    }
    deleteApi({ ids: data.selectedRowKeys.join(',') })
        .then((res) => {
            message.success('删除成功');
            data.selectedRowKeys = [];
            getDataList();
        })
        .catch((err) => {
            message.error(err.msg || '操作失败');
        });
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
            formData.append('description', modal.form.description || '')

            if (modal.editFlag) {
                updateApi(formData)
                    .then((res) => {
                        hideModal();
                        getDataList();
                    })
                    .catch((err) => {
                        console.log(err);
                        message.error(err.msg || '操作失败');
                    });
            } else {
                createApi(formData)
                    .then((res) => {
                        hideModal();
                        getDataList();
                    })
                    .catch((err) => {
                        console.log(err);
                        message.error(err.msg || '操作失败');
                    });
            }
        })
        .catch((err) => {
            console.log('不能为空');
        });
};

const handleCancel = () => {
    hideModal();
};

// 恢复表单初始状态
const resetModal = () => {
    myform.value?.resetFields();
    fileList.value = []
};

// 关闭弹窗
const hideModal = () => {
    modal.visile = false;
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

.table-operations>button {
    margin-right: 8px;
}
</style>
  