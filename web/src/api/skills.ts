import {get, post} from '/@/utils/http/axios';

enum URL {
    userList = '/api/skills/list',
    createSkill = '/api/skills/create',
    updateSkill = '/api/skills/update',
    delete = '/api/skills/delete',
}

const listApi = async (params: any) =>
    get<any>({url: URL.userList, params: params, data: {}, headers: {}});
const createSkillApi = async (data: any) =>
    post<any>({
        url: URL.createSkill,
        params: {},
        data: data,
        headers: {'Content-Type': 'multipart/form-data;charset=utf-8'}
    });
const updateSkillApi = async (data: any) =>
    post<any>({
        url: URL.updateSkill,
        data: data,
        headers: {'Content-Type': 'multipart/form-data;charset=utf-8'}
    });
const deleteApi = async (params: any) =>
    post<any>({url: URL.delete, params: params, headers: {}});

export {listApi, createSkillApi, updateSkillApi, deleteApi};
