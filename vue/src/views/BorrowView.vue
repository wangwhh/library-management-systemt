<template>
    <div>
        <div  class="insert" style="margin-left: 10px;margin-bottom: 10px">
            <el-button type="primary" @click="dialogFormVisible = true" icon="el-icon-circle-plus-outline">借书</el-button>
            <el-dialog title="借书" :visible.sync="dialogFormVisible">
                <el-form :model="insertForm">
                    <el-form-item label="借书证编号" :label-width="formLabelWidth">
                        <el-input v-model="insertForm.cardId" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="图书编号" :label-width="formLabelWidth">
                        <el-input v-model="insertForm.bookId" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="借用时间" :label-width="formLabelWidth">
                        <el-date-picker v-model="insertForm.borrow_time" autocomplete="off"></el-date-picker>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">取 消</el-button>
                    <el-button type="primary" @click="insert(), dialogFormVisible = false">确 定</el-button>
                </div>
            </el-dialog>
        </div>


        <el-table :data="tableData" height="800px"
                  border style="width: 100%">
            <el-table-column
                prop="cardId"
                label="借书卡编号"
                sortable>
            </el-table-column>
            <el-table-column
                prop="bookId"
                label="图书编号"
                sortable>
            </el-table-column>
            <el-table-column prop="borrowTime" label="借用时间" sortable>
                <template slot-scope="scope">
                    <span v-if="scope.row.borrowTime !== 0" >
                      {{ parseTime(scope.row.borrowTime, "{y}-{m}-{d}") }}
                    </span>
                </template>
            </el-table-column>
            <el-table-column prop="returnTime" label="归还时间" sortable>
                <template slot-scope="scope">
                    <span v-if="scope.row.returnTime !== 0">
                      {{ parseTime(scope.row.returnTime, "{y}-{m}-{d}") }}
                    </span>
                    <span v-else>
                        未归还
                    </span>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
                <template slot-scope="scope">
                    <el-button @click="returnFormVisible = true, fillReturn(scope.row)"
                               type="text" size="small"
                    :disabled="scope.row.returnTime!==0">还书</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-dialog title="还书" :visible.sync="returnFormVisible">
            <el-form :model="returnForm">
                <el-form-item label="归还时间" :label-width="formLabelWidth">
                    <el-date-picker v-model="returnForm.return_time" type="date"></el-date-picker>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="returnFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="return_book(), returnFormVisible = false">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import axios from "axios";
import {parseTime} from "@/utils/date";

export default {
    data() {
        return {
            dialogFormVisible: false,
            insertForm: {
                cardId: '',
                bookId: '',
                borrow_time: '',
            },
            returnFormVisible: false,
            returnForm: {
                cardId: '',
                bookId: '',
                borrow_time: '',
                return_time: '',
            },
            formLabelWidth: '100px',
            tableData: [],

        }
    },
    created() {
        this.load()
    },
    methods: {
        parseTime,
        load(){
            fetch('http://localhost:9090/borrow/list')
                .then(res=>res.json()).then(res=>{
                console.log(res)
                this.tableData = res
            })
        },
        insert(){
            const borrow={
                cardId: this.insertForm.cardId,
                bookId: this.insertForm.bookId,
                borrowTime: new Date(this.insertForm.borrow_time).getTime()
            }
            axios.post('http://localhost:9090/borrow/insert', borrow)
                .then((response)=> {
                    console.log(response);
                    if(response.data.ok === true){
                        this.$notify({
                            title: '成功',
                            message: '借书成功！',
                            type: 'success'
                        });
                        this.load();
                    }else{
                        this.$notify.error({
                            title: '错误',
                            message: response.data.message
                        });
                    }
                })
                .catch((error)=> {
                    console.log(error);
                    alert('输入格式错误');
                });
        },
        fillReturn(row){
            this.returnForm = Object.assign({}, row);
        },
        return_book(){
            const borrow={
                cardId: this.returnForm.cardId,
                bookId: this.returnForm.bookId,
                returnTime: new Date(this.returnForm.return_time).getTime()
            }
            axios.post('http://localhost:9090/borrow/ret', borrow)
                .then((response)=> {
                    console.log(response);
                    if(response.data.ok === true){
                        this.$notify({
                            title: '成功',
                            message: '还书成功！',
                            type: 'success'
                        });
                        this.load();
                    }else{
                        this.$notify.error({
                            title: '错误',
                            message: response.data.message
                        });
                    }
                })
                .catch((error)=> {
                    console.log(error);
                    alert('输入格式错误');
                });
        }
    }
}
</script>