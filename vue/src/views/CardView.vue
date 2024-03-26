<template>
    <div>
        <div class="insert" style="margin-left: 10px;margin-bottom: 10px">
            <el-button type="primary" @click="insertFormVisible = true" icon="el-icon-circle-plus-outline">注册借书证</el-button>
            <el-dialog title="注册借书证" :visible.sync="insertFormVisible">
                <el-form :model="insertForm">
                    <el-form-item label="姓名" :label-width="formLabelWidth">
                        <el-input v-model="insertForm.name" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="学院" :label-width="formLabelWidth">
                        <el-input v-model="insertForm.dept" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="身份" :label-width="formLabelWidth">
                        <el-radio v-model="insertForm.type" label="Student">学生</el-radio>
                        <el-radio v-model="insertForm.type" label="Teacher">教师</el-radio>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="insertFormVisible = false">取 消</el-button>
                    <el-button type="primary" @click="insert(), insertFormVisible = false">确 定</el-button>
                </div>
            </el-dialog>
        </div>


        <el-table
                :data="tableData"
                height="800px"
                border
                style="width: 100%"
                :default-sort = "{prop: 'cardId', order: 'ascending'}">
            <el-table-column
                prop="cardId"
                label="编号"
                sortable>
            </el-table-column>
            <el-table-column
                prop="name"
                label="姓名"
                sortable>
            </el-table-column>
            <el-table-column
                prop="department"
                label="学院"
                sortable>
            </el-table-column>
            <el-table-column
                prop="type"
                label="类型"
                sortable>
            </el-table-column>
            <el-table-column label="操作" width="100">
                <template slot-scope="scope">
                    <el-button type="text" @click="dialogTableVisible = true, showHistory(scope.row.cardId)">借书历史</el-button>
                    <el-button type="text" @click="deleteCard(scope.row.cardId)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog title="借书历史" :visible.sync="dialogTableVisible">
            <el-table :data="borrowHistory">
                <el-table-column property="bookId" label="图书编号"></el-table-column>
                <el-table-column property="borrowTime" label="借用时间">
                    <template slot-scope="scope">
                        <span v-if="scope.row.borrowTime !== 0" >
                          {{ parseTime(scope.row.borrowTime, "{y}-{m}-{d}") }}
                        </span>
                    </template>
                </el-table-column>
                <el-table-column property="returnTime" label="归还时间">
                    <template slot-scope="scope">
                        <span v-if="scope.row.returnTime !== 0">
                          {{ parseTime(scope.row.returnTime, "{y}-{m}-{d}") }}
                        </span>
                        <span v-else>
                            未归还
                        </span>
                    </template>
                </el-table-column>
            </el-table>
        </el-dialog>
    </div>
</template>

<script>


import axios from "axios";
import {parseTime} from "@/utils/date";
import {del} from "vue";

export default {
    data() {
        return {
            insertForm:{
                name: '',
                dept: '',
                type: ''
            },
            formLabelWidth: '100px',
            insertFormVisible: false,
            dialogTableVisible: false,
            borrowHistory: [],
            tableData: []
        }
    },
    created() {
        this.load()
    },
    methods: {
        del,
        parseTime,
        load(){
            fetch('http://localhost:9090/card/list')
                .then(res=>res.json()).then(res=>{
                console.log(res)
                this.tableData = res
            })
        },
        insert(){
            const card={
                name: this.insertForm.name,
                department: this.insertForm.dept,
                type: this.insertForm.type
            }
            axios.post('http://localhost:9090/card/insert', card)
                .then((response)=> {
                    console.log(response);
                    if(response.data.ok === true){
                        this.$notify({
                            title: '成功',
                            message: '注册成功！',
                            type: 'success'
                        });
                        this.load();
                    }else{
                        this.$notify.error({
                            title: '错误',
                            message: '借书卡已存在！'
                        });
                    }
                })
                .catch((error)=> {
                    console.log(error);
                    alert('输入格式错误');
                });
        },
        showHistory(cardId){
            const obj={cardId: cardId};
            axios.post('http://localhost:9090/card/postid', obj)
                .then((response)=> {
                    console.log(response);
                    this.loadHistory();
                })
                .catch((error)=> {
                    console.log(error);
                    alert('查询失败！');
                })
        },
        loadHistory(){
            fetch('http://localhost:9090/card/histories')
                .then(res=>res.json()).then(res=> {
                console.log(res)
                this.borrowHistory = res
            })
        },
        deleteCard(cardId){
            this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                const obj={
                    cardId: cardId
                };
                axios.post('http://localhost:9090/card/del', obj)
                    .then((response)=> {
                        console.log(response);
                        if(response.data.ok === true){
                            this.$notify({
                                title: '删除成功',
                                type: 'success',
                                message: '借书证已删除!'
                            });
                            this.load();
                        }else{
                            this.$notify.error({
                                title: '删除失败',
                                message: '还有未归还的书！'
                            });
                        }
                    })
                    .catch((error)=> {
                        console.log(error);
                        alert('删除失败！连接错误！');
                    });

            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        }
    }
}
</script>