<template>
  <div class="content-list">
    <div>
      <div class="list-title">我的简历</div>
      <a-spin :spinning="loading" style="min-height: 200px;">
        <div class="list-content">
          <div class="edit-view">
            <div class="item flex-view">
              <div class="label required">真实姓名</div>
              <div class="right-box">
                <input
                  type="text"
                  v-model="tData.form.name"
                  placeholder="请输入姓名"
                  maxlength="20"
                  class="input-dom"
                />
                <p class="tip">支持中英文，长度不能超过 20 个字符</p>
              </div>
            </div>
            <div class="item flex-view">
              <div class="label required">创建角色</div>
              <div class="right-box">
                <button class="custom-button" @click="showModal = true">设置角色能力</button>
              </div>
            </div>
            <a-modal
              class="modern-modal"
              title="设置角色能力"
              :visible="showModal"
              width="880px"
              @ok="handleOk"
              @cancel="showModal = false"
            >
              <div class="modal-content">
                <div class="grid">
                  <div class="column">
                    <h3>专业技能</h3>
                    <div class="input-group">
                      <label>技术能力</label>
                      <input type="number" v-model="data.dataList.technicalSkills" />
                    </div>
                    <div class="input-group">
                      <label>证书资质</label>
                      <input type="number" v-model="data.dataList.certifications" />
                    </div>
                  </div>
                  <div class="column">
                    <h3>软技能</h3>
                    <div class="input-group">
                      <label>沟通能力</label>
                      <input type="number" v-model="data.dataList.communication" />
                    </div>
                    <div class="input-group">
                      <label>团队合作</label>
                      <input type="number" v-model="data.dataList.teamwork" />
                    </div>
                    <div class="input-group">
                      <label>问题解决</label>
                      <input type="number" v-model="data.dataList.problemSolving" />
                    </div>
                    <div class="input-group">
                      <label>领导力</label>
                      <input type="number" v-model="data.dataList.leadership" />
                    </div>
                  </div>
                  <div class="column">
                    <h3>学习能力</h3>
                    <div class="input-group">
                      <label>持续学习</label>
                      <input type="number" v-model="data.dataList.continuousLearning" />
                    </div>
                    <div class="input-group">
                      <label>适应能力</label>
                      <input type="number" v-model="data.dataList.adaptability" />
                    </div>
                  </div>
                  <div class="column">
                    <h3>工作经验</h3>
                    <div class="input-group">
                      <label>相关经验</label>
                      <input type="number" v-model="data.dataList.relevantExperience" />
                    </div>
                    <div class="input-group">
                      <label>成就展示</label>
                      <input type="number" v-model="data.dataList.achievements" />
                    </div>
                  </div>
                  <div class="column">
                    <h3>个人特质</h3>
                    <div class="input-group">
                      <label>责任心</label>
                      <input type="number" v-model="data.dataList.responsibility" />
                    </div>
                    <div class="input-group">
                      <label>抗压能力</label>
                      <input type="number" v-model="data.dataList.stressManagement" />
                    </div>
                    <div class="input-group">
                      <label>诚信正直</label>
                      <input type="number" v-model="data.dataList.integrity" />
                    </div>
                  </div>
                  <div class="column">
                    <h3>行业知识</h3>
                    <div class="input-group">
                      <label>行业洞察</label>
                      <input type="number" v-model="data.dataList.industryInsights" />
                    </div>
                    <div class="input-group">
                      <label>法规政策</label>
                      <input type="number" v-model="data.dataList.regulatoryKnowledge" />
                    </div>
                  </div>
                </div>
              </div>
            </a-modal>
            <div class="item flex-view">
              <div class="label required">简历文件</div>
              <div class="right-box" style="display: flex;flex-direction: row;">
                <a-upload
                  name="file1"
                  :multiple="false"
                  :before-upload="beforeRawUpload"
                  :show-upload-list="showUpload"
                >
                  <a-button>
                    <upload-outlined></upload-outlined>点击选择文件
                  </a-button>（请上传 pdf 文件）
                </a-upload>
                <a v-if="tData.form.raw" :href="tData.form.downloadUrl" style="margin-top: 6px;">下载</a>
              </div>
            </div>
            <button class="save mg" @click="submit()">保存</button>
          </div>
        </div>
      </a-spin>
    </div>
  </div>
</template>

<script setup>
import { message } from 'ant-design-vue';
import { detailApi, createApi, updateApi } from '/@/api/resume';
import { BASE_URL } from '/@/store/constants';
import { useUserStore } from '/@/store';
import { listApi, updateSkillApi, createSkillApi } from '/@/api/skills';
import AvatarIcon from '/@/assets/images/avatar.jpg';

const router = useRouter();
const userStore = useUserStore();
const showModal = ref(false);

let loading = ref(false);
let tData = reactive({
  form: {
    id: undefined,
    name: undefined,
    sex: undefined,
    birthday: undefined,
    education: undefined,
    cover: undefined,
    coverFile: undefined,
    rawFile: undefined,
    raw: undefined,
    email: undefined,
    mobile: undefined,
    description: undefined,
    downloadUrl: undefined,
  },
});

// 页面数据
const data = reactive({
  dataList: [],
  loading: false,
  keyword: '',
});

let showUpload = true;

onMounted(() => {
  console.log(userStore.user_id);
  getDetail();
  getDataList();
});

const getDataList = () => {
  data.loading = true;
  listApi({
    keyword: data.keyword,
  })
    .then((res) => {
      data.loading = false;
      console.log(res.data);
      for (let i = 0; i < res.data.length; i++) {
        //将res.data[i].userId转换成number类型
        console.log(res.data[i].userId);
        if (Number(userStore.user_id) === Number(res.data[i].userId)) {
          data.dataList = res.data[i];
          break;
        }
      }
      console.log(data.dataList.username);
    })
    .catch((err) => {
      data.loading = false;
      console.log(err);
    });
};

const handleOk = () => {
  const formData = new FormData();
  formData.append('userId', userStore.user_id);
  formData.append('technicalSkills', data.dataList.technicalSkills);
  formData.append('certifications', data.dataList.certifications);
  formData.append('communication', data.dataList.communication);
  formData.append('teamwork', data.dataList.teamwork);
  formData.append('problemSolving', data.dataList.problemSolving);
  formData.append('leadership', data.dataList.leadership);
  formData.append('continuousLearning', data.dataList.continuousLearning);
  formData.append('adaptability', data.dataList.adaptability);
  formData.append('relevantExperience', data.dataList.relevantExperience);
  formData.append('achievements', data.dataList.achievements);
  formData.append('responsibility', data.dataList.responsibility);
  formData.append('stressManagement', data.dataList.stressManagement);
  formData.append('integrity', data.dataList.integrity);
  formData.append('industryInsights', data.dataList.industryInsights);
  formData.append('regulatoryKnowledge', data.dataList.regulatoryKnowledge);
  if (data.dataList.id) {
    formData.append('id', data.dataList.id);
    console.log('update');
    updateSkillApi(formData)
    .then((res) => {
      message.success('保存成功');
      getDataList();
      showModal.value = false;
    })
    .catch((err) => {
      console.log(err);
    });
  } else {
    createSkillApi(formData)
    .then((res) => {
      message.success('保存成功');
      getDataList();
      showModal.value = false;
    })
    .catch((err) => {
      console.log(err);
    });
  }
  
};

const beforeRawUpload = (file) => {
  // 改文件名
  showUpload = true;
  const fileName = new Date().getTime().toString() + '.' + file.type.substring(12);
  const copyFile = new File([file], fileName);
  console.log(copyFile);
  if (copyFile) {
    tData.form.rawFile = copyFile;
  }
  return false;
};

const getDetail = () => {
  loading.value = true;
  let userId = userStore.user_id;
  detailApi({ userId: userId })
    .then((res) => {
      if (res.data) {
        tData.form = res.data;
        if (tData.form.raw !== undefined) {
          tData.form.downloadUrl = BASE_URL + '/api/staticfiles/resume/raw/' + tData.form.raw;
        }
      }

      loading.value = false;
    })
    .catch((err) => {
      console.log(err);
      loading.value = false;
    });
};
const submit = () => {
  let formData = new FormData();
  let userId = userStore.user_id;
  formData.append('userId', userId);

  if (tData.form.id) {
    formData.append('id', tData.form.id);
  }

  if (!tData.form.name) {
    message.warn('请填写真实姓名');
    showUpload = false;
    return;
  }
  if (!tData.form.rawFile) {
    message.warn('请上传简历文件');
    return;
  }

  formData.append('rawFile', tData.form.rawFile);
  if (tData.form.rawFile.name.substr(-3, 3) !== 'pdf') {
    // console.log(tData.form.rawFile.name.substr(-3, 3))
    message.warn('上传的文件不为 pdf 文件，保存失败');
    showUpload = false;
    return;
  }

  formData.append('name', tData.form.name);

  if (tData.form.id) {
    updateApi(formData)
      .then((res) => {
        message.success('保存成功');
        getDetail();
      })
      .catch((err) => {
        console.log(err);
      });
  } else {
    createApi(formData)
      .then((res) => {
        message.success('保存成功');
        getDetail();
      })
      .catch((err) => {
        console.log(err);
      });

    showUpload = false;
  }
};
</script>

<style scoped lang="less">
input,
textarea {
  border-style: none;
  outline: none;
  margin: 0;
  padding: 0;
}

.flex-view {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
}

.required::before {
  color: #ff4d4f;
  content: '*';
  display: inline-block;
  font-family: SimSun, sans-serif;
  font-size: 14px;
  line-height: 1;
  margin-right: 4px;
}

.custom-button {
  background-color: #4684e2; /* 背景颜色 */
  color: #fff; /* 文字颜色 */
  border: none; /* 去掉边框 */
  border-radius: 4px; /* 圆角 */
  padding: 10px 20px; /* 内边距 */
  font-size: 14px; /* 字体大小 */
  cursor: pointer; /* 鼠标样式 */
  transition: background-color 0.3s; /* 过渡效果 */

  &:hover {
    background-color: #3a6ec0; /* 悬停时的背景颜色 */
  }

  &:active {
    background-color: #2e5ba0; /* 点击时的背景颜色 */
  }
}

.modern-modal {
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  .ant-model-title {
    text-align: center; /* 居中对齐 */
    font-weight: bold; /* 加粗 */
  }
}

.modal-content {
  padding: 20px;
}

.grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}

.column {
  flex: 1;
}

.modal-content h3 {
  font-size: 1.2rem;
  margin-bottom: 10px;
  color: #333;
  font-weight: bold;
}

.input-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 15px;
}

.input-group label {
  font-size: 1rem;
  margin-bottom: 5px;
  color: #555;
}

.input-group input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 1rem;
  outline: none;
  transition: border-color 0.3s;
}

.input-group input:focus {
  border-color: #007bff;
}

.content-list {
  -webkit-box-flex: 1;
  -ms-flex: 1;
  flex: 1;

  .list-title {
    color: #152844;
    font-weight: 600;
    font-size: 18px;
    line-height: 48px;
    height: 48px;
    margin-bottom: 4px;
    border-bottom: 1px solid #cedce4;
  }

  .edit-view {
    .item {
      -webkit-box-align: center;
      -ms-flex-align: center;
      align-items: center;
      margin: 24px 0;

      .label {
        width: 100px;
        color: #152844;
        font-weight: 600;
        font-size: 14px;
      }

      .right-box {
        -webkit-box-flex: 1;
        -ms-flex: 1;
        flex: 1;
      }

      .avatar {
        width: 64px;
        height: 64px;
        border-radius: 50%;
        margin-right: 16px;
      }

      .change-tips {
        -webkit-box-align: center;
        -ms-flex-align: center;
        align-items: center;
        -ms-flex-wrap: wrap;
        flex-wrap: wrap;
      }

      label {
        color: #4684e2;
        font-size: 14px;
        line-height: 22px;
        height: 22px;
        cursor: pointer;
        width: 100px;
        display: block;
      }

      .tip {
        color: #6f6f6f;
        font-size: 14px;
        height: 22px;
        line-height: 22px;
        margin: 0;
        width: 100%;
      }

      .right-box {
        -webkit-box-flex: 1;
        -ms-flex: 1;
        flex: 1;
      }

      .input-dom {
        width: 400px;
      }

      .input-dom {
        background: #f8fafb;
        border-radius: 4px;
        height: 40px;
        line-height: 40px;
        font-size: 14px;
        color: #152844;
        padding: 0 12px;
      }

      .tip {
        font-size: 12px;
        line-height: 16px;
        color: #6f6f6f;
        height: 16px;
        margin-top: 4px;
      }

      .intro {
        resize: none;
        background: #f8fafb;
        width: 100%;
        padding: 8px 12px;
        height: 82px;
        line-height: 22px;
        font-size: 14px;
        color: #152844;
      }
    }

    .save {
      background: #4684e2;
      border-radius: 32px;
      width: 96px;
      height: 32px;
      line-height: 32px;
      font-size: 14px;
      color: #fff;
      border: none;
      outline: none;
      cursor: pointer;
    }

    .mg {
      margin-left: 100px;
    }
  }
}
</style>
