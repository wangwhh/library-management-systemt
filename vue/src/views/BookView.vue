<template>
    <div>
        <!-- 查询和插入 -->
        <div class="search" style="margin-left: 10px;margin-bottom: 10px">
            <el-input
                placeholder="分类"
                prefix-icon="el-icon-search"
                v-model="search_category"
                style="width: 150px; margin-right: 10px; margin-bottom: 10px"
                clearable>
            </el-input>
            <el-input
                placeholder="书名"
                prefix-icon="el-icon-search"
                v-model="search_title"
                style="width: 150px; margin-right: 10px; margin-bottom: 10px"
                clearable>
            </el-input>
            <el-input
                placeholder="出版社"
                prefix-icon="el-icon-search"
                v-model="search_press"
                style="width: 150px; margin-right: 10px; margin-bottom: 10px"
                clearable>
            </el-input>
            <el-input
                placeholder="作者"
                prefix-icon="el-icon-search"
                v-model="search_author"
                style="width: 150px; margin-right: 10px; margin-bottom: 10px"
                clearable>
            </el-input>

            <el-input
                v-model="search_year1"
                type="year"
                placeholder="年份下界"
                style="width: 150px; margin-left: 10px; margin-bottom: 10px"
                clearable>
            </el-input>
            -
            <el-input
                v-model="search_year2"
                type="year"
                placeholder="年份上界"
                style="width: 150px; margin-right: 10px; margin-bottom: 10px"
                clearable>
            </el-input>

            <el-input
                placeholder="价格下界"
                prefix-icon="el-icon-search"
                v-model="search_price1"
                style="width: 150px; margin-left: 10px; margin-bottom: 10px"
                clearable>
            </el-input>
            -
            <el-input
                placeholder="价格上界"
                prefix-icon="el-icon-search"
                v-model="search_price2"
                style="width: 150px; margin-right: 10px; margin-bottom: 10px"
                clearable>
            </el-input>
            <el-button @click="search()" type="primary" icon="el-icon-search">查询</el-button>

            <!-- 插入 -->
            <el-button type="primary" @click="dialogFormVisible = true" icon="el-icon-circle-plus-outline">插入图书</el-button>
            <el-dialog title="插入图书" :visible.sync="dialogFormVisible">
                <el-form :model="insertform">
                    <el-form-item label="分类" :label-width="formLabelWidth">
                        <el-input v-model="insertform.category" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="书名" :label-width="formLabelWidth">
                        <el-input v-model="insertform.title" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="出版社" :label-width="formLabelWidth">
                        <el-input v-model="insertform.press" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="出版年份" :label-width="formLabelWidth">
                        <el-input v-model.number="insertform.year" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="作者" :label-width="formLabelWidth">
                        <el-input v-model="insertform.author" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="价格" :label-width="formLabelWidth">
                        <el-input v-model.number="insertform.price" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="库存" :label-width="formLabelWidth">
                        <el-input v-model.number="insertform.stock" autocomplete="off"></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">取 消</el-button>
                    <el-button type="primary" @click="insert(), dialogFormVisible = false">确 定</el-button>
                </div>
            </el-dialog>
        </div>

        <!-- 表格体 -->
        <el-table
                :data="tableData"
                height="800px"
                border
                style="width: 100%"
                :default-sort = "{prop: 'bookId', order: 'ascending'}">
            <el-table-column
                fixed
                prop="bookId"
                label="编号"
                sortable
                width="100">
            </el-table-column>
            <el-table-column
                prop="category"
                label="分类"
                sortable>
            </el-table-column>
            <el-table-column
                prop="title"
                label="书名"
                sortable>
            </el-table-column>
            <el-table-column
                prop="press"
                label="出版社"
                sortable>
            </el-table-column>
            <el-table-column
                prop="publishYear"
                label="出版年份"
                width="130"
                sortable>
            </el-table-column>
            <el-table-column
                prop="author"
                label="作者"
                sortable>
            </el-table-column>
            <el-table-column
                prop="price"
                label="价格"
                sortable>
            </el-table-column>
            <el-table-column
                prop="stock"
                label="库存"
                sortable
                width="100">
            </el-table-column>
            <el-table-column
                label="操作"
                width="100">
                <template slot-scope="scope">
                    <el-button @click="editFormVisible = true, fillEditForm(scope.row)" type="text" size="small">编辑</el-button>
                    <el-button @click="handle_delete(scope.row.bookId)" type="text" size="small">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-dialog title="编辑信息" :visible.sync="editFormVisible">
            <el-form :model="editform">
                <el-form-item label="分类" :label-width="formLabelWidth">
                    <el-input v-model="editform.category" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="书名" :label-width="formLabelWidth">
                    <el-input v-model="editform.title" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="出版社" :label-width="formLabelWidth">
                    <el-input v-model="editform.press" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="出版年份" :label-width="formLabelWidth">
                    <el-input v-model.number="editform.publishYear" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="作者" :label-width="formLabelWidth">
                    <el-input v-model="editform.author" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="价格" :label-width="formLabelWidth">
                    <el-input v-model.number="editform.price" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="库存" :label-width="formLabelWidth">
                    <el-input v-model.number="editform.stock" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="editFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="handle_edit(), editFormVisible = false">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import axios from "axios";

export default {
    data() {
        return {
            search_category:'',
            search_title:'',
            search_press:'',
            search_year1:'',
            search_year2:'',
            search_author:'',
            search_price1:'',
            search_price2:'',

            dialogFormVisible: false,
            insertform: {
                category: '',
                title: '',
                press: '',
                year: '',
                author:'',
                price:'',
                stock:'',
            },

            editFormVisible: false,
            editform: {
                bookId: '',
                category: '',
                title: '',
                press: '',
                publishYear: '',
                author: '',
                price: '',
                stock: ''
            },

            formLabelWidth: '80px',
            tableData: [],
        }
    },
    created() {
        this.load()
    },
    methods: {
        load(){
            fetch('http://localhost:9090/book/list')
                .then(res=>res.json()).then(res=>{
                    console.log(res)
                this.tableData = res
            })
        },
        search(){
            const conditions={
                category: this.search_category,
                title: this.search_title,
                press: this.search_press,
                minPublishYear: this.search_year1,
                maxPublishYear: this.search_year2,
                author: this.search_author,
                minPrice: this.search_price1,
                maxPrice: this.search_price2,
            };
            axios.post('http://localhost:9090/book/search', conditions)
                .then((response)=> {
                    console.log(response);
                    if(response.data.ok === true){
                        this.$notify({
                            title: '搜索成功',
                            message: '图书搜索成功！',
                            type: 'success'
                        });
                        this.load();
                    }else{
                        this.$notify.error({
                            title: '错误',
                            message: '搜索失败！'
                        });
                    }

                })
                .catch((error)=> {
                    console.log(error);
                    alert('输入格式错误');
                });
        },
        insert(){
            const book={
                category: this.insertform.category,
                title: this.insertform.title,
                press: this.insertform.press,
                publishYear: this.insertform.publishYear,
                author: this.insertform.author,
                price: this.insertform.price,
                stock: this.insertform.stock,
            };
            axios.post('http://localhost:9090/book/insert', book)
                .then((response)=> {
                    console.log(response);
                    if(response.data.ok === true){
                        this.$notify({
                            title: '插入成功',
                            message: '图书添加成功！',
                            type: 'success'
                        });
                        this.load();
                    }else{
                        this.$notify.error({
                            title: '错误',
                            message: '图书已存在！'
                        });
                    }
                })
                .catch((error)=> {
                    console.log(error);
                    alert('输入格式错误');
                });
        },
        handle_delete(bookId){
            this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                const obj={
                    bookId: bookId
                };
                axios.post('http://localhost:9090/book/del', obj)
                    .then((response)=> {
                        console.log(response);
                        if(response.data.ok === true){
                            this.$notify({
                                title: '删除成功',
                                type: 'success',
                                message: '图书已删除!'
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
        },
        fillEditForm(row){
            this.editform = Object.assign({}, row);
        },
        handle_edit(){
            const book={
                bookId: this.editform.bookId,
                category: this.editform.category,
                title: this.editform.title,
                press: this.editform.press,
                publishYear: this.editform.publishYear,
                author: this.editform.author,
                price: this.editform.price,
                stock: this.editform.stock
            };
            axios.post('http://localhost:9090/book/edit', book)
                .then((response)=> {
                    console.log(response);
                    if(response.data.ok === true){
                        this.$notify({
                            title: '修改成功',
                            message: '信息修改成功！',
                            type: 'success'
                        });
                        this.load();
                    }else{
                        this.$notify.error({
                            title: '错误',
                            message: '图书已存在！'
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