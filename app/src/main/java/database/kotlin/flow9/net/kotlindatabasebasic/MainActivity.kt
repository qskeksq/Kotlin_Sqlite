package database.kotlin.flow9.net.kotlindatabasebasic

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var dbHelper: DbHelper_file? = null
    private var userList: ArrayList<User>? = null
    private var userAdapter: UserAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 헬퍼 생성
        dbHelper = DbHelper_file(this)
        // 유저 데이터 쿼리
        dbHelper?.readableDatabase
//        userList = dbHelper?.getUser()
        // 리사이클러 어댑처 생성
        userAdapter = UserAdapter(this, userList)
        // 어댑터 설정
        userRecyclerview.adapter = userAdapter
        // 레이아웃 매니저 설정
        userRecyclerview.layoutManager = LinearLayoutManager(this)

        setListener()
    }

    private fun setListener() {
        add.setOnClickListener{
            add()
        }

        update.setOnClickListener{
            update()
        }

        delete.setOnClickListener{
            delete()
        }
    }

    private fun add() {
        // 입력한 값 가져오기
        val id = inputId.text.toString()
        val password = inputPassword.text.toString()
        val name = inputName.text.toString()
        val age = inputAge.text.toString().toInt()
        // User 객체 생성
        val user = User(id, name, password, age)
        // 추가
        dbHelper?.addUser(user)
        // 화면에 보이는 리사이클러뷰의 갱신
        updateList()
    }

    private fun update() {
        // 입력한 값 가져오기
        val id = inputId.text.toString()
        val password = inputPassword.text.toString()
        val name = inputName.text.toString()
        val age = inputAge.text.toString().toInt()
        // 유저 객체 생성
        val user = User(id, password, name, age)
        // 업데이트
        dbHelper?.updateUser(user)
        // 화면에 보이는 리사이클러뷰의 갱신
        updateList()
    }

    private fun delete() {
        // 삭제할 유저의 아이디
        val id = inputId.text.toString()
        // 삭제
        dbHelper?.deleteUser(id)
        // 화면에 보이는 리사이클러뷰의 갱신
        updateList()
    }

    private fun updateList() {
        userList = dbHelper?.getUser()
        userAdapter?.updateData(userList)
    }

}
