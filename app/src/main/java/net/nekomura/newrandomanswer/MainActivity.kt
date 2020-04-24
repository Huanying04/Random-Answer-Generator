package net.nekomura.newrandomanswer

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.View.OnClickListener
import android.widget.*
import androidx.appcompat.widget.AlertDialogLayout
import androidx.appcompat.widget.Toolbar

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.setting_dialogue.*
import org.w3c.dom.Text
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity()/*, View.OnClickListener*/ {
    var type : Int = 0
    var qNum : Int = 0
    var Answers4 = arrayOf("A", "B", "C", "D")
    var Answers5 = arrayOf("A", "B", "C", "D", "E")
    var truefalse = arrayOf("〇", "×")
    val upperAlphaRegex = Regex("[A-Z]+")
    var summontypes = arrayOf(
        "單選題(A-D)",
        "單選題(A-E)",
        "單選題(第一選項, 最後選項)",
        "多選題(A-D)",
        "多選題(A-E)",
        "多選題(第一選項, 最後選項)",
        "多選項填空(第一選項, 最後選項)",
        "多選項填空(全部選項)",
        "是非題(〇, ×)",
        "隨機數字(最大值)",
        "隨機數字(最大值, 最小值)")
    var rand: Int = 0
    /*lateinit var cb_d_A : CheckBox
    lateinit var cb_d_B : CheckBox
    lateinit var cb_d_C : CheckBox
    lateinit var cb_d_D : CheckBox*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var toolbar2: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar2)

        //main_summonType!!.setOnItemSelectedListener(this)
        val spinnerList = ArrayAdapter(this, android.R.layout.simple_spinner_item, summontypes)
        spinnerList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        main_summonType.setAdapter(spinnerList)

        main_summonType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent:AdapterView<*>?, view: View?, position: Int, id: Long) {
                type = position
                if (type == 2) { //單選題(第一選項, 最後選項)
                    RandIntMax.visibility = View.INVISIBLE
                    RandIntMin.visibility = View.INVISIBLE
                    RandAnsFirst.visibility = View.VISIBLE
                    RandAnsLast.visibility = View.VISIBLE
                    RandAnsAll.visibility = View.INVISIBLE
                    RandAnsAllHint.visibility = View.INVISIBLE
                }else if (type == 5) { //多選題(第一選項, 最後選項)
                    RandIntMax.visibility = View.INVISIBLE
                    RandIntMin.visibility = View.INVISIBLE
                    RandAnsFirst.visibility = View.VISIBLE
                    RandAnsLast.visibility = View.VISIBLE
                    RandAnsAll.visibility = View.INVISIBLE
                    RandAnsAllHint.visibility = View.INVISIBLE
                }else if (type == 6) { //多選項填空(第一選項, 最後選項)
                    RandIntMax.visibility = View.INVISIBLE
                    RandIntMin.visibility = View.INVISIBLE
                    RandAnsFirst.visibility = View.VISIBLE
                    RandAnsLast.visibility = View.VISIBLE
                    RandAnsAll.visibility = View.INVISIBLE
                    RandAnsAllHint.visibility = View.INVISIBLE
                }else if (type == 7) { //多選項填空(全部選項)
                    RandIntMax.visibility = View.INVISIBLE
                    RandIntMin.visibility = View.INVISIBLE
                    RandAnsFirst.visibility = View.INVISIBLE
                    RandAnsLast.visibility = View.INVISIBLE
                    RandAnsAll.visibility = View.VISIBLE
                    RandAnsAllHint.visibility = View.VISIBLE
                }else if (type == 9) { //隨機數字(最大值)
                    RandIntMax.visibility = View.VISIBLE
                    RandIntMin.visibility = View.INVISIBLE
                    RandAnsFirst.visibility = View.INVISIBLE
                    RandAnsLast.visibility = View.INVISIBLE
                    RandAnsAll.visibility = View.INVISIBLE
                    RandAnsAllHint.visibility = View.INVISIBLE
                }else if (type == 10) { //隨機數字(最大值, 最小值)
                    RandIntMax.visibility = View.VISIBLE
                    RandIntMin.visibility = View.VISIBLE
                    RandAnsFirst.visibility = View.INVISIBLE
                    RandAnsLast.visibility = View.INVISIBLE
                    RandAnsAll.visibility = View.INVISIBLE
                    RandAnsAllHint.visibility = View.INVISIBLE
                }else {
                    RandIntMax.visibility = View.INVISIBLE
                    RandIntMin.visibility = View.INVISIBLE
                    RandAnsFirst.visibility = View.INVISIBLE
                    RandAnsLast.visibility = View.INVISIBLE
                    RandAnsAll.visibility = View.INVISIBLE
                    RandAnsAllHint.visibility = View.INVISIBLE
                }
            }
        }

        summonButton.setOnClickListener {
            if (type == 0) {  //單選題(A-D)
                qNum++
                rand = (0 until Answers4.size).random()
                answerLabel.textSize = 270.toFloat()
                answerLabel.setText(Answers4[rand])
                QNumNumBox.setText(qNum.toString())
            }else if (type == 1){  //單選題(A-E)
                qNum++
                rand = (0 until Answers5.size).random()
                answerLabel.textSize = 270.toFloat()
                answerLabel.setText(Answers5[rand])
                QNumNumBox.setText(qNum.toString())
            }else if (type == 2){  //單選題(第一選項, 最後選項)
                try {
                    qNum++
                    var randAnsFirst :Char = RandAnsFirst.getText().toString().get(0)
                    var randAnsLast :Char = RandAnsLast.getText().toString().get(0)
                    var allAnsArray = ArrayList<Char>()
                    var displayAns: String = ""

                    if (randAnsFirst.toString().matches(upperAlphaRegex) && randAnsLast.toString().matches(upperAlphaRegex)) {
                        if (randAnsFirst <= randAnsLast) {
                            for (ch: Char in randAnsFirst..randAnsLast) {
                                allAnsArray.add(ch)
                            }

                            rand = (0 until allAnsArray.size).random()

                            answerLabel.textSize = 270.toFloat()
                            answerLabel.setText(allAnsArray.get(rand).toString())
                            QNumNumBox.setText(qNum.toString())
                        }else {
                            var builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("第一個英文字母必須比最後一個小")
                            builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                            builder.show()
                        }
                    }else {
                        var builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("只能輸入大寫英文字母")
                        builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                        builder.show()
                    }
                }catch (e: StringIndexOutOfBoundsException) {
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle("錯誤!")
                    builder.setMessage("輸入值不得為空")
                    builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                    builder.show()
                }catch (e: Throwable) {
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle("錯誤!")
                    builder.setMessage("出現未知錯誤，錯誤代碼: \n" + e.toString())
                    builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                    builder.show()
                }
            }else if (type == 3){  //多選題(A-D)
                qNum++

                var FiveAnsArray = ArrayList<Char>()
                FiveAnsArray.add('A')
                FiveAnsArray.add('B')
                FiveAnsArray.add('C')
                FiveAnsArray.add('D')
                var FirstAnsStr: String = ""
                var LastAnsStr: String = ""
                var howManyAnsrate: Int = (1..100).random()
                var howManyAns: Int = 1
                if (howManyAnsrate >= 1 && howManyAnsrate <= 8) {
                    howManyAns = 1
                }else if (howManyAnsrate >= 9 && howManyAnsrate <= 50) {
                    howManyAns = 2
                }else if (howManyAnsrate >= 51 && howManyAnsrate <= 92) {
                    howManyAns = 3
                }else if (howManyAnsrate >= 93 && howManyAnsrate <= 100) {
                    howManyAns = 4
                }else {
                    howManyAns = 4
                }

                for (i: Int in 0 until howManyAns) {
                    var rand: Int = (0 until FiveAnsArray.size).random()
                    FirstAnsStr = FirstAnsStr + FiveAnsArray.get(rand)
                    FiveAnsArray.remove(FiveAnsArray.get(rand))
                }

                for (i: Int in 0 until FiveAnsArray.size) {
                    FiveAnsArray.remove(FiveAnsArray.get(0))
                }

                FiveAnsArray.add('A')
                FiveAnsArray.add('B')
                FiveAnsArray.add('C')
                FiveAnsArray.add('D')

                for (i: Int in 0 until FiveAnsArray.size) {
                    if (FirstAnsStr.indexOf(FiveAnsArray.get(i)) != -1) {
                        LastAnsStr = LastAnsStr + FiveAnsArray.get(i)
                    }
                }

                answerLabel.textSize = 90.toFloat()
                answerLabel.setText(LastAnsStr)
                QNumNumBox.setText(qNum.toString())
            }else if (type == 4){ //多選題(A-E)
                qNum++

                var FiveAnsArray = ArrayList<Char>()
                FiveAnsArray.add('A')
                FiveAnsArray.add('B')
                FiveAnsArray.add('C')
                FiveAnsArray.add('D')
                FiveAnsArray.add('E')
                var FirstAnsStr: String = ""
                var LastAnsStr: String = ""
                var howManyAnsrate: Int = (1..100).random()
                var howManyAns: Int = 1
                if (howManyAnsrate >= 1 && howManyAnsrate <= 5) {
                    howManyAns = 1
                }else if (howManyAnsrate >= 6 && howManyAnsrate <= 35) {
                    howManyAns = 2
                }else if (howManyAnsrate >= 36 && howManyAnsrate <= 65) {
                    howManyAns = 3
                }else if (howManyAnsrate >= 66 && howManyAnsrate <= 95) {
                    howManyAns = 4
                }else if (howManyAnsrate >= 96 && howManyAnsrate <= 100) {
                    howManyAns = 5
                }else {
                    howManyAns = 5
                }

                for (i: Int in 0 until howManyAns) {
                    var rand: Int = (0 until FiveAnsArray.size).random()
                    FirstAnsStr = FirstAnsStr + FiveAnsArray.get(rand)
                    FiveAnsArray.remove(FiveAnsArray.get(rand))
                }

                for (i: Int in 0 until FiveAnsArray.size) {
                    FiveAnsArray.remove(FiveAnsArray.get(0))
                }

                FiveAnsArray.add('A')
                FiveAnsArray.add('B')
                FiveAnsArray.add('C')
                FiveAnsArray.add('D')
                FiveAnsArray.add('E')

                for (i: Int in 0 until FiveAnsArray.size) {
                    if (FirstAnsStr.indexOf(FiveAnsArray.get(i)) != -1) {
                        LastAnsStr = LastAnsStr + FiveAnsArray.get(i)
                    }
                }

                answerLabel.textSize = 90.toFloat()
                answerLabel.setText(LastAnsStr)
                QNumNumBox.setText(qNum.toString())
            }else if (type == 5){  //多選題(第一選項, 最後選項)
                try {
                    qNum++
                    var randAnsFirst :Char = RandAnsFirst.getText().toString().get(0)
                    var randAnsLast :Char = RandAnsLast.getText().toString().get(0)
                    var allAnsArray = ArrayList<Char>()
                    var howManyAns = (1..randAnsLast.toInt()-randAnsFirst.toInt()).random()
                    var displayAns: String = ""
                    var firstAns: String = ""
                    var lastAns: String = ""

                    if (randAnsFirst.toString().matches(upperAlphaRegex) && randAnsLast.toString().matches(upperAlphaRegex)) {
                        if (randAnsFirst <= randAnsLast) {
                            for (ch: Char in randAnsFirst..randAnsLast) {
                                allAnsArray.add(ch)
                            }

                            rand = (0 until allAnsArray.size).random()

                            for (i: Int in 0 until howManyAns) {
                                var rand: Int = (0 until allAnsArray.size).random()
                                firstAns = firstAns + allAnsArray.get(rand)
                                allAnsArray.removeAt(rand)
                            }

                            allAnsArray = ArrayList<Char>()

                            for (ch: Char in randAnsFirst..randAnsLast) {
                                allAnsArray.add(ch)
                            }

                            for (i: Int in 0 until allAnsArray.size) {
                                if (firstAns.indexOf(allAnsArray.get(i)) != -1) {
                                    lastAns = lastAns + allAnsArray.get(i)
                                }
                            }

                            answerLabel.textSize = 60.toFloat()
                            answerLabel.setText(lastAns)
                            QNumNumBox.setText(qNum.toString())
                        }else {
                            var builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("第一個英文字母必須比最後一個小")
                            builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                            builder.show()
                        }
                    }else {
                        var builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("只能輸入大寫英文字母")
                        builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                        builder.show()
                    }
                }catch (e: StringIndexOutOfBoundsException) {
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle("錯誤!")
                    builder.setMessage("輸入值不得為空")
                    builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                    builder.show()
                }catch (e: Throwable) {
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle("錯誤!")
                    builder.setMessage("出現未知錯誤，錯誤代碼: \n" + e.toString())
                    builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                    builder.show()
                }
            }else if (type == 6){  //多選項填空(第一選項, 最後選項)
                try {
                    qNum++
                    var randAnsFirst :Char = RandAnsFirst.getText().toString().get(0)
                    var randAnsLast :Char = RandAnsLast.getText().toString().get(0)
                    var allAnsArray = ArrayList<Char>()
                    var displayAns: String = ""

                    if (randAnsFirst.toString().matches(upperAlphaRegex) && randAnsLast.toString().matches(upperAlphaRegex)) {
                        if (randAnsFirst <= randAnsLast) {
                            for (ch: Char in randAnsFirst..randAnsLast) {
                                allAnsArray.add(ch)
                            }

                            for (i: Int in 0 until allAnsArray.size) {
                                rand = (0 until allAnsArray.size).random()
                                displayAns = displayAns + allAnsArray.get(rand)
                                allAnsArray.removeAt(rand)
                            }
                            answerLabel.textSize = 30.toFloat()
                            answerLabel.setText(displayAns)
                            QNumNumBox.setText(qNum.toString())
                        }else {
                            var builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("第一個英文字母必須比最後一個小")
                            builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                            builder.show()
                        }
                    }else {
                        var builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("只能輸入大寫英文字母")
                        builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                        builder.show()
                    }
                }catch (e: StringIndexOutOfBoundsException) {
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle("錯誤!")
                    builder.setMessage("輸入值不得為空")
                    builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                    builder.show()
                }catch (e: Throwable) {
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle("錯誤!")
                    builder.setMessage("出現未知錯誤，錯誤代碼: \n" + e.toString())
                    builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                    builder.show()
                }
            }else if (type == 7){  //多選項填空(全部選項)
                try {
                    qNum++
                    var allAnsArray = ArrayList<Char>()
                    var displayAns: String = ""

                    for (i: Int in 0 until RandAnsAll.getText().toString().length) {
                        allAnsArray.add(RandAnsAll.getText().toString().get(i))
                    }
                    for (i: Int in 0 until allAnsArray.size) {
                        rand = (0 until allAnsArray.size).random()
                        displayAns = displayAns + allAnsArray.get(rand)
                        allAnsArray.removeAt(rand)
                    }

                    if (!(displayAns.equals(""))) {
                        answerLabel.textSize = 30.toFloat()
                        answerLabel.setText(displayAns)
                        QNumNumBox.setText(qNum.toString())
                    }else {
                        var builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("輸入值不得為空")
                        builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                        builder.show()
                    }
                }catch (e: Throwable) {
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle("錯誤!")
                    builder.setMessage("出現未知錯誤，錯誤代碼: \n" + e.toString())
                    builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                    builder.show()
                }
            }else if (type == 8){  //是非題
                qNum++
                rand = (0 until truefalse.size).random()
                answerLabel.textSize = 270.toFloat()
                answerLabel.setText(truefalse[rand])
                QNumNumBox.setText(qNum.toString())
            }else if (type == 9){
                try {
                    var randomIntMax :Int = RandIntMax.getText().toString().toInt()

                    if (randomIntMax < 1) {
                        var builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("輸入值不得小於1，若最大值慾設為0，請使用\"隨機數字(最大值, 最小值)\"")
                        builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                        builder.show()
                    }else {
                        qNum++
                        rand = (1..randomIntMax).random()
                        answerLabel.textSize = 60.toFloat()
                        answerLabel.setText(rand.toString())
                        QNumNumBox.setText(qNum.toString())
                    }
                }catch (e: NoSuchElementException) {
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle("錯誤!")
                    builder.setMessage("輸入值不得大於2147483647")
                    builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                    builder.show()
                }catch (e: NumberFormatException) {
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle("錯誤!")
                    builder.setMessage("輸入值不得為空")
                    builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                    builder.show()
                }catch (e: Throwable) {
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle("錯誤!")
                    builder.setMessage("出現未知錯誤，錯誤代碼: \n" + e.toString())
                    builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                    builder.show()
                }
            }else if (type == 10){
                try {
                    var randomIntMax :Int = RandIntMax.getText().toString().toInt()
                    var randomIntMin :Int = RandIntMin.getText().toString().toInt()
                    if (randomIntMin > randomIntMax) {
                        var builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("最小值不得大於最大值")
                        builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                        builder.show()
                    }else {
                        qNum++
                        rand = (randomIntMin until randomIntMax+1).random()
                        answerLabel.textSize = 60.toFloat()
                        answerLabel.setText(rand.toString())
                        QNumNumBox.setText(qNum.toString())
                    }
                }catch (e: NoSuchElementException) {
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle("錯誤!")
                    builder.setMessage("輸入值不得大於2147483647")
                    builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                    builder.show()
                }catch (e: NumberFormatException) {
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle("錯誤!")
                    builder.setMessage("輸入值不得為空")
                    builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                    builder.show()
                }catch (e: Throwable) {
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle("錯誤!")
                    builder.setMessage("出現未知錯誤，錯誤代碼: \n" + e.toString())
                    builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                    builder.show()
                }
            }else {
                var builder = AlertDialog.Builder(this)
                builder.setTitle("錯誤!")
                builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
                builder.show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i: Int = item.getItemId()

        /*if (i == R.id.action_setting) {
            /*var builder = AlertDialog.Builder(this)
            builder.setTitle("設定")
            builder.setView(layoutInflater.inflate(R.layout.setting_dialogue, null))
            builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
            builder.setNegativeButton("取消", {dialogInterface: DialogInterface, i: Int ->})
            builder.setCancelable(false)
            /*val yesButton = builder.create()
            yesButton.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

            }*/
            builder.show()*/
        }else */if (i == R.id.action_about) {
            var builder = AlertDialog.Builder(this)
            //userInput.setText(Html.fromHtml(getResources().getString(R.string.action_about)))
            builder.setTitle("關於")
            builder.setMessage(R.string.about_string)
            builder.setCancelable(true)
            //builder.setView(R.layout.activity_about)
            builder.setPositiveButton("確定", {dialogInterface: DialogInterface, i: Int ->})
            var finalAlert: AlertDialog = builder.create()
            finalAlert.show()
            (finalAlert.findViewById(android.R.id.message) as TextView).setMovementMethod(LinkMovementMethod.getInstance())
        }

        return super.onOptionsItemSelected(item)
    }

    /*override fun onClick(v: View?) {
        v as CheckBox
        var isChecked : Boolean = v.isChecked
        when (v.id) {
            R.id.deleteA -> if (isChecked) {
            }
        }
    }*/
}