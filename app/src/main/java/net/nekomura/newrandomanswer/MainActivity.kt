package net.nekomura.newrandomanswer

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.content_main.*

/**
 * @author 貓村幻影
 * @version 1.0.6.4
 */
class MainActivity : AppCompatActivity() {
    //題號
    private var qNum : Int = 0
    //4個選項的陣列
    private var answers4 = arrayOf("A", "B", "C", "D")
    //5個選項的陣列
    private var answers5 = arrayOf("A", "B", "C", "D", "E")
    //是非題陣列
    private var truefalse = arrayOf("O", "X")
    //生成模式
    private val summonTypes = arrayOf(
        "單選題(A-D)",
        "單選題(A-E)",
        "單選題(第一選項, 最後選項)",
        "多選題(A-D)",
        "多選題(A-E)",
        "多選題(第一選項, 最後選項)",
        "多選項填空(第一選項, 最後選項)",
        "多選項填空(全部選項)",
        "是非題(O, X)",
        "隨機數字(最大值)",
        "隨機數字(最大值, 最小值)")
    //生成歷史
    private var summonHistory = StringBuffer()

    //RandAnsFirst的TextWatcher，自動將RandAnsFirst的文字改成大寫
    private val randAnsFirstListener = object: TextWatcher {

        override fun afterTextChanged(s: Editable) {}

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            //如果RandAnsFirst不為空
            if (s.isNotEmpty()) {
                //先取消監聽
                RandAnsFirst.removeTextChangedListener(this)
                //將第一個字符轉為大寫(因為設定長度最多為1所以只需要設定第1個即可)
                RandAnsFirst.setText(s[0].toUpperCase().toString(), TextView.BufferType.EDITABLE)
                //將輸入位置調至後
                RandAnsFirst.setSelection(RandAnsFirst.length())
                //重新裝上監聽
                RandAnsFirst.addTextChangedListener(this)
            }
        }
    }

    //RandAnsLast的TextWatcher，自動將RandAnsLast的文字改成大寫
    private val randAnsLastListener = object : TextWatcher {

        override fun afterTextChanged(s: Editable) {}

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            //如果RandAnsLast不為空
            if (s.isNotEmpty()) {
                //先取消監聽
                RandAnsLast.removeTextChangedListener(this)
                //將第一個字符轉為大寫(因為設定長度最多為1所以只需要設定第1個即可)
                RandAnsLast.setText(s[0].toUpperCase().toString(), TextView.BufferType.EDITABLE)
                //將輸入位置調至後
                RandAnsLast.setSelection(RandAnsLast.length())
                //重新裝上監聽
                RandAnsLast.addTextChangedListener(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //設定toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //設定生成模式下拉式選項
        val spinnerList = ArrayAdapter(this, android.R.layout.simple_spinner_item, summonTypes)
        spinnerList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        main_summonType.adapter = spinnerList

        //生成模式選項選取監聽器
        main_summonType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                showSimpleAlertDialog("錯誤!", "請選擇正確的生成選項", "確定", null)
            }

            //當選擇一個生成模式選項時
            override fun onItemSelected(parent:AdapterView<*>?, view: View?, position: Int, id: Long) {
                //對應不同生成模式顯示不同的物件
                when (main_summonType.selectedItem.toString()) {
                    "單選題(第一選項, 最後選項)" -> { //單選題(第一選項, 最後選項)
                        invisibleAll()
                        RandAnsFirst.visibility = View.VISIBLE
                        RandAnsLast.visibility = View.VISIBLE
                    }
                    "多選題(第一選項, 最後選項)" -> { //多選題(第一選項, 最後選項)
                        invisibleAll()
                        RandAnsFirst.visibility = View.VISIBLE
                        RandAnsLast.visibility = View.VISIBLE
                    }
                    "多選項填空(第一選項, 最後選項)" -> { //多選項填空(第一選項, 最後選項)
                        invisibleAll()
                        RandAnsFirst.visibility = View.VISIBLE
                        RandAnsLast.visibility = View.VISIBLE
                    }
                    "多選項填空(全部選項)" -> { //多選項填空(全部選項)
                        invisibleAll()
                        RandAnsAll.visibility = View.VISIBLE
                        RandAnsAllHint.visibility = View.VISIBLE
                    }
                    "隨機數字(最大值)" -> { //隨機數字(最大值)
                        invisibleAll()
                        RandIntMax.visibility = View.VISIBLE
                    }
                    "隨機數字(最大值, 最小值)" -> { //隨機數字(最大值, 最小值)
                        invisibleAll()
                        RandIntMax.visibility = View.VISIBLE
                        RandIntMin.visibility = View.VISIBLE
                    }
                    else -> {
                        invisibleAll()
                    }
                }
            }
        }

        //自動將RandAnsFirst的文字改成大寫
        RandAnsFirst.addTextChangedListener(randAnsFirstListener)

        //自動將RandAnsLast的文字改成大寫
        RandAnsLast.addTextChangedListener(randAnsLastListener)

        //當按下"生成!"按鈕時
        summonButton.setOnClickListener {
            try {
                //當選中某個選項時
                when (main_summonType.selectedItem.toString()) {
                    "單選題(A-D)" -> {  //單選題(A-D)
                        //生成隨機選項
                        val rand = (answers4.indices).random()
                        generateAnswer(270f, answers4[rand], answers4[rand], false)
                    }
                    "單選題(A-E)" -> {  //單選題(A-E)
                        //生成隨機選項
                        val rand = (answers5.indices).random()
                        generateAnswer(270f, answers5[rand], answers5[rand], false)
                    }
                    "單選題(第一選項, 最後選項)" -> {  //單選題(第一選項, 最後選項)
                        //如果RandAnsFirst和RandAnsLast皆不為空
                        if (RandAnsFirst.text.isNotEmpty() && RandAnsLast.text.isNotEmpty()) {
                            val randAnsFirst: Char = RandAnsFirst.text.toString()[0]
                            val randAnsLast: Char = RandAnsLast.text.toString()[0]
                            val allAnsArray = ArrayList<Char>()

                            //如果randAnsFirst和randAnsLast都是大寫英文字母
                            if (CharUtils().isEnglishLetter(randAnsFirst) && CharUtils().isEnglishLetter(randAnsLast)) {
                                if (randAnsFirst <= randAnsLast) {  //如果randAnsFirst比randAnsLast小或相同
                                    for (ch: Char in randAnsFirst..randAnsLast) {
                                        allAnsArray.add(ch)
                                    }
                                    //生成隨機選項
                                    val rand = (0 until allAnsArray.size).random()
                                    generateAnswer(
                                        270f,
                                        allAnsArray[rand].toString(),
                                        allAnsArray[rand].toString(),
                                        false
                                    )
                                } else  //如果randAnsFirst比randAnsLast大
                                //彈出錯誤視窗
                                    showSimpleAlertDialog(
                                        "錯誤!",
                                        "第一個英文字母必須比最後一個小",
                                        "確定",
                                        null
                                    )
                            } else {  //如果randAnsFirst和randAnsLast其一不是大寫英文字母
                                //彈出錯誤視窗
                                showSimpleAlertDialog("錯誤!", "只能輸入英文字母", "確定", null)
                            }
                        }else {  //如果randAnsFirst和randAnsLast其一為空
                            //彈出錯誤視窗
                            showSimpleAlertDialog("錯誤!", "輸入值不得為空", "確定", null)
                        }
                    }
                    "多選題(A-D)" -> {  //多選題(A-D)
                        val set = randomMultiple(arrayListOf('A', 'B', 'C', 'D'))
                        generateAnswer(90f, set, "[$set]", true)
                    }
                    "多選題(A-E)" -> { //多選題(A-E)
                        val set = randomMultiple(arrayListOf('A', 'B', 'C', 'D', 'E'))
                        generateAnswer(90f, set, "[$set]", true)
                    }
                    "多選題(第一選項, 最後選項)" -> {  //多選題(第一選項, 最後選項)
                        //如果RandAnsFirst和RandAnsLast皆不為空
                        if (RandAnsFirst.text.isNotEmpty() && RandAnsLast.text.isNotEmpty()) {
                            val randAnsFirst: Char = RandAnsFirst.text.toString()[0]
                            val randAnsLast: Char = RandAnsLast.text.toString()[0]
                            val allAnsArray = ArrayList<Char>()

                            //如果randAnsFirst和randAnsLast都是英文字母
                            if (CharUtils().isEnglishLetter(randAnsFirst) && CharUtils().isEnglishLetter(randAnsLast)) {
                                //如果randAnsFirst比randAnsLast小或相同
                                if (randAnsFirst <= randAnsLast) {
                                    //將randAnsFirst到randAnsLast中的所有字母塞到一個ArrayList中
                                    for (ch in randAnsFirst..randAnsLast) {
                                        allAnsArray.add(ch)
                                    }

                                    //生成隨機選項，最少生成1選項，最多全選
                                    val set = randomMultiple(1, randAnsLast - (randAnsFirst - 1), allAnsArray)
                                    generateAnswer(60f, set, "[$set]", true)
                                } else  //如果randAnsFirst比randAnsLast大
                                    showSimpleAlertDialog(
                                        "錯誤!",
                                        "第一個英文字母必須比最後一個小",
                                        "確定",
                                        null
                                    )
                            } else
                                showSimpleAlertDialog("錯誤!",
                                    "只能輸入英文字母",
                                    "確定",
                                    null)
                        } else {
                            showSimpleAlertDialog("錯誤!",
                                "輸入值不得為空",
                                "確定",
                                null)
                        }
                    }
                    "多選項填空(第一選項, 最後選項)" -> {  //多選項填空(第一選項, 最後選項)
                        //如果RandAnsFirst和RandAnsLast皆不為空
                        if (RandAnsFirst.text.isNotEmpty() && RandAnsLast.text.isNotEmpty()) {
                            val randAnsFirst: Char = RandAnsFirst.text.toString()[0]
                            val randAnsLast: Char = RandAnsLast.text.toString()[0]

                            if (CharUtils().isEnglishLetter(randAnsFirst) && CharUtils().isEnglishLetter(randAnsLast)) {
                                if (randAnsFirst <= randAnsLast) {
                                    val set = cloze(randAnsFirst, randAnsLast)
                                    generateAnswer(30f, set, "[$set]",true)
                                }else
                                    showSimpleAlertDialog(
                                        "錯誤!",
                                        "第一個英文字母必須比最後一個小",
                                        "確定",
                                        null
                                    )
                            }else
                                showSimpleAlertDialog(
                                    "錯誤!",
                                    "只能輸入英文字母",
                                    "確定",
                                    null)
                        }else {
                            showSimpleAlertDialog(
                                "錯誤!",
                                "輸入值不得為空",
                                "確定",
                                null)
                        }
                    }
                    "多選項填空(全部選項)" -> {  //多選項填空(全部選項)
                        //如果RandAnsAll不為空
                        if (RandAnsAll.text.isNotEmpty()) {
                            val array = StringUtils().toCharArrayList(RandAnsAll.text.toString())
                            val set = cloze(array)
                            generateAnswer(30f, set, "[$set]", true)
                        }else {  //如果RandAnsAll為空，則彈出錯誤視窗
                            showSimpleAlertDialog("錯誤!", "輸入值不得為空", "確定", null)
                        }
                    }
                    "是非題(O, X)" -> {  //是非題
                        val rand = (truefalse.indices).random()
                        generateAnswer(270f, truefalse[rand], truefalse[rand], false)
                    }
                    "隨機數字(最大值)" -> {  //最大值隨機數字
                        //如果RandIntMax不為空
                        if (RandIntMax.text.isNotEmpty()) {
                            //檢測RandIntMax值的範圍
                            when (val randomNumMax = RandIntMax.text.toString().toInt()) {
                                //若在1~999999999之間，則隨機生成數字
                                in 1..999999999 -> {
                                    val rand = (1..randomNumMax).random()
                                    generateAnswer(60f, rand.toString(), "($rand)", true)
                                }
                                //若小於1，則彈出錯誤視窗
                                in Integer.MIN_VALUE..0 -> {
                                    showSimpleAlertDialog(
                                        "錯誤!",
                                        "輸入值不得小於1，若最大值慾設為0，請使用\"隨機數字(最大值, 最小值)\"",
                                        "確定",
                                        null
                                    )
                                }
                                //若大於等於1000000000，則彈出錯誤視窗
                                in 1000000000..Integer.MAX_VALUE -> {
                                    showSimpleAlertDialog(
                                        "錯誤!",
                                        "輸入值不得大於999999999",
                                        "確定",
                                        null
                                    )
                                }
                            }
                        }else {
                            showSimpleAlertDialog("錯誤!", "輸入值不得為空", "確定", null)
                        }
                    }
                    "隨機數字(最大值, 最小值)" -> {  //最小值-最大值隨機數字
                        if (RandIntMax.text.isNotEmpty() && RandIntMin.text.isNotEmpty()) {
                            val randomIntMax: Int = RandIntMax.text.toString().toInt()
                            val randomIntMin: Int = RandIntMin.text.toString().toInt()
                            if (randomIntMin > randomIntMax)
                                showSimpleAlertDialog("錯誤!", "最小值不得大於最大值", "確定", null)
                            else if (randomIntMin > 999999999 || randomIntMax > 999999999) {
                                showSimpleAlertDialog(
                                    "錯誤!",
                                    "輸入值不得大於999999999",
                                    "確定",
                                    null
                                )
                            }
                            else {
                                val rand = (randomIntMin..randomIntMax).random()
                                generateAnswer(60f, rand.toString(), "($rand)", true)
                            }
                        }
                    }
                    else ->
                        showSimpleAlertDialog("錯誤!", "請選擇正確的生成選項", "確定", null)
                }
            }catch (err: Throwable) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("錯誤!")
                builder.setMessage("$err")
                builder.setCancelable(true)
                builder.setPositiveButton("確定") { _: DialogInterface, _: Int ->}
                builder.show()
            }
        }

        //當按下生成歷史按鈕
        summonHistoryButton.setOnClickListener {
            showAlertDialog("生成歷史", summonHistory.toString(), false, 0, true, "確定", null, false, null, null, true, "複製到剪貼簿",
                {
                        _: DialogInterface, _: Int ->
                    val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText(summonHistory.toString(), summonHistory.toString())
                    clipboard.setPrimaryClip(clip)
                })
        }

        //當按下歸零按鈕
        ResetButton.setOnClickListener{
            //彈出警告窗口
            showAlertDialog("警告", "這個動作會使\"生成歷史\"及\"題號\"通通歸零，將不會保存。是否繼續？", true, android.R.attr.alertDialogIcon, true, "確定",
                {
                        _: DialogInterface, _: Int ->
                    qNum = 0
                    QNumNumBox.text = qNum.toString()
                    summonHistory = StringBuffer()
                    val toast = Toast.makeText(applicationContext, "已歸零", Toast.LENGTH_SHORT)
                    toast.show()
                }, true, "取消", null, false, null, null)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    //當選擇Options Item時
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            //關於
            R.id.action_about -> {
                val finalAlert: AlertDialog = setAlertDialog("關於",
                    R.string.about_string,
                    false,
                    0,
                    true,
                    "確定",
                    null,
                    false,
                    null,
                    null ,
                    false,
                    null,
                    null).create()
                finalAlert.show()
                (finalAlert.findViewById(android.R.id.message) as TextView).movementMethod =
                    LinkMovementMethod.getInstance()
            }
            /*R.id.action_save -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("存檔")
                builder.setView(R.id.action_save)
                builder.setCancelable(true)
                builder.setPositiveButton("確定") { dialogInterface: DialogInterface, int: Int ->
                    var save = JSONObject()
                    save.put("name", filename.text)
                    save.put("history", summonHistory)

                    val sd_main = getExternalFilesDir("saves")

                    //TODO 存檔系統

                }
            }*/
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * 將所有輸入框隱藏
     */
    fun invisibleAll() {
        RandIntMax.visibility = View.INVISIBLE
        RandIntMin.visibility = View.INVISIBLE
        RandAnsFirst.visibility = View.INVISIBLE
        RandAnsLast.visibility = View.INVISIBLE
        RandAnsAll.visibility = View.INVISIBLE
        RandAnsAllHint.visibility = View.INVISIBLE
    }

    /**
     * 當按下生成按鈕時新的生成選項並加到生成歷史中
     *
     * @param textSize 設定answerLabel文字大小
     * @param display 設定answerLabel的文字
     * @param history 增加到生成歷史的字符串
     * @param forceNewline 新增生成歷史後強制換行
     */
    private fun generateAnswer(textSize: Float, display: String, history: String, forceNewline: Boolean) {
        //題號+1
        qNum++
        //設定answerLabel文字大小
        answerLabel.textSize = textSize
        //設定answerLabel的文字
        answerLabel.text = display

        if (forceNewline) {  //如果要強制換行
            //如果生成歷史的最後一行已經有字符了，則換新的一行
            if (summonHistory.split("\n")[summonHistory.split("\n").size - 1].isNotEmpty())
                summonHistory.append("\n")
            //將history加至生成歷史
            summonHistory.append(history)
            //生成歷史換行
            summonHistory.append("\n")
        } else {  //如果不用強制換行
            //將history加至生成歷史
            summonHistory.append(history)
            //如果append後該行滿5個字符，則換新的一行
            if (summonHistory.split("\n")[summonHistory.split("\n").size - 1].length % 5 == 0)
                summonHistory.append("\n")
        }

        //顯示新的題號
        QNumNumBox.text = qNum.toString()
    }

    /**
     * 設定AlertDialog
     *
     * @param title 標題
     * @param message 內容
     * @param setIconAttribute 是否設置圖標
     * @param iconAttribute 圖標
     * @param setPositiveButton 是否設置Positive Button
     * @param positiveButtonText Positive Button的文字
     * @param positiveButtonListener 按下Positive Button後執行的程式碼
     * @param setNegativeButton 是否設置Negative Button
     * @param negativeButtonText Negative Button的文字
     * @param negativeButtonListener 按下Negative Button後執行的程式碼
     * @param setNeutralButton 是否設置Neutral Button
     * @param neutralButtonText Neutral Button的文字
     * @param neutralButtonListener 按下Neutral Button後執行的程式碼
     *
     * @return 建置完成的AlertDialog.Builder
     */
    private fun setAlertDialog(title: String?,
                               message: String?,
                               setIconAttribute: Boolean,
                               iconAttribute: Int,
                               setPositiveButton: Boolean,
                               positiveButtonText: String?,
                               positiveButtonListener: DialogInterface.OnClickListener?,
                               setNegativeButton: Boolean,
                               negativeButtonText: String?,
                               negativeButtonListener: DialogInterface.OnClickListener?,
                               setNeutralButton: Boolean,
                               neutralButtonText: String?,
                               neutralButtonListener: DialogInterface.OnClickListener?): AlertDialog.Builder {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        if (setIconAttribute)
            builder.setIconAttribute(iconAttribute)
        if (setPositiveButton)
            builder.setPositiveButton(positiveButtonText, positiveButtonListener)
        if (setNegativeButton)
            builder.setNegativeButton(negativeButtonText, negativeButtonListener)
        if (setNeutralButton)
            builder.setNeutralButton(neutralButtonText, neutralButtonListener)

        return builder
    }

    private fun setAlertDialog(title: String?,
                               @StringRes message: Int,
                               setIconAttribute: Boolean,
                               iconAttribute: Int,
                               setPositiveButton: Boolean,
                               positiveButtonText: String?,
                               positiveButtonListener: DialogInterface.OnClickListener?,
                               setNegativeButton: Boolean,
                               negativeButtonText: String?,
                               negativeButtonListener: DialogInterface.OnClickListener?,
                               setNeutralButton: Boolean,
                               neutralButtonText: String?,
                               neutralButtonListener: DialogInterface.OnClickListener?): AlertDialog.Builder {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        if (setIconAttribute)
            builder.setIconAttribute(iconAttribute)
        if (setPositiveButton)
            builder.setPositiveButton(positiveButtonText, positiveButtonListener)
        if (setNegativeButton)
            builder.setPositiveButton(negativeButtonText, negativeButtonListener)
        if (setNeutralButton)
            builder.setPositiveButton(neutralButtonText, neutralButtonListener)

        return builder
    }

    /**
     * 顯示AlertDialog
     *
     * @param title 標題
     * @param message 內容
     * @param setIconAttribute 是否設置圖標
     * @param iconAttribute 圖標
     * @param setPositiveButton 是否設置Positive Button
     * @param positiveButtonText Positive Button的文字
     * @param positiveButtonListener 按下Positive Button後執行的程式碼
     * @param setNegativeButton 是否設置Negative Button
     * @param negativeButtonText Negative Button的文字
     * @param negativeButtonListener 按下Negative Button後執行的程式碼
     * @param setNeutralButton 是否設置Neutral Button
     * @param neutralButtonText Neutral Button的文字
     * @param neutralButtonListener 按下Neutral Button後執行的程式碼
     */
    private fun showAlertDialog(title: String?,
                                message: String?,
                                setIconAttribute: Boolean,
                                iconAttribute: Int,
                                setPositiveButton: Boolean,
                                positiveButtonText: String?,
                                positiveButtonListener: DialogInterface.OnClickListener?,
                                setNegativeButton: Boolean,
                                negativeButtonText: String?,
                                negativeButtonListener: DialogInterface.OnClickListener?,
                                setNeutralButton: Boolean,
                                neutralButtonText: String?,
                                neutralButtonListener: DialogInterface.OnClickListener?){
        setAlertDialog(title, message, setIconAttribute, iconAttribute, setPositiveButton, positiveButtonText, positiveButtonListener, setNegativeButton, negativeButtonText, negativeButtonListener, setNeutralButton, neutralButtonText, neutralButtonListener).show()
    }

    /**
     * 設定簡單AlertDialog
     *
     * @param title 標題
     * @param message 內容
     * @param positiveButtonText Positive Button的文字
     * @param positiveButtonListener 按下Positive Button後執行的程式碼
     */
    fun showSimpleAlertDialog(title: String?,
                               message: String?,
                               positiveButtonText: String,
                               positiveButtonListener: DialogInterface.OnClickListener?) {
        setAlertDialog(title, message, true, 0, true, positiveButtonText, positiveButtonListener, false, null, null, false, null, null).show()
    }

    /**
     * 多選題
     * @param array 所有可能選項的陣列
     *
     * @return 隨機生成的多選選項
     */
    private fun randomMultiple(array: ArrayList<Char>): String {
        return randomMultiple(2, array.size, array)
    }

    /**
     * 多選題
     * @param min 最少生成幾個選項
     * @param max 最多生成幾個選項
     * @param array 所有可能選項的陣列
     *
     * @return 隨機生成的多選選項
     */
    private fun randomMultiple(min: Int, max: Int, array: ArrayList<Char>): String {
        //隨機選取生成幾個選項(應選選項數)
        val howManyAnswerMustChoose = (min..max).random()
        val answer = ArrayList<Char>()
        val sb = StringBuffer()

        //重複應選選項數次
        for (i in 0 until howManyAnswerMustChoose) {
            val rand = (0 until array.size).random()
            answer.add(array[rand])
            array.remove(array[rand])
        }

        //使用冒泡排列重新排列生成的選項
        for (i in answer.indices) {
            sb.append(Sort().bubble(answer)[i])
        }

        return sb.toString()
    }

    /**
     * 克漏字(完形填空) (當選項皆為英文字母且連續)
     * @param first 第一選項
     * @param last 最後選項
     */
    private fun cloze(first: Char, last: Char): String {
        val array = ArrayList<Char>()
        val sb = StringBuffer()

        for (ch in first..last) {
            array.add(ch)
        }

        for (i in 0 until array.size) {
            val rand = (0 until array.size).random()
            sb.append(array[rand])
            array.removeAt(rand)
        }

        return sb.toString()
    }

    /**
     * 克漏字(完形填空) (當選項非英文字母或不連續)
     */
    private fun cloze(array: ArrayList<Char>): String {
        val sb = StringBuffer()

        for (i in 0 until array.size) {
            val rand = (0 until array.size).random()
            sb.append(array[rand])
            array.removeAt(rand)
        }

        return sb.toString()
    }
}