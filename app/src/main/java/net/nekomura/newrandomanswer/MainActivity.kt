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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.content_main.*
import net.nekomura.newrandomanswer.CharUtils.isEnglishLetter
import net.nekomura.newrandomanswer.StringUtils.toCharArrayList

/**
 * @author 貓村幻影
 */
class MainActivity : AppCompatActivity() {
    //題號
    private var qNum : Int = 0
    //生成歷史
    private var summonHistory = StringBuffer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //設定toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //設定生成模式下拉式選項
        val spinnerList = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayOf(
            getString(R.string.multiple_choice_A_D),
            getString(R.string.multiple_choice_A_E),
            getString(R.string.multiple_choice_custom),
            getString(R.string.multiple_selection_A_D),
            getString(R.string.multiple_selection_A_E),
            getString(R.string.multiple_selection_custom),
            getString(R.string.cloze_custom),
            getString(R.string.cloze_custom_all),
            getString(R.string.yes_or_no),
            getString(R.string.random_number_max),
            getString(R.string.random_number_min_max)
        ))
        spinnerList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        summonTypeSpinner.adapter = spinnerList

        //生成模式選項選取監聽器
        summonTypeSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                showSimpleAlertDialog(getString(R.string.error), getString(R.string.warning_incorrect_selection), getString(R.string.confirm))
            }

            //當選擇一個生成模式選項時
            override fun onItemSelected(parent:AdapterView<*>?, view: View?, position: Int, id: Long) {
                //對應不同生成模式顯示不同的物件
                when (summonTypeSpinner.selectedItem.toString()) {
                    getString(R.string.multiple_choice_custom) -> { //單選題(第一選項, 最後選項)
                        invisibleAll()
                        randomAnswerFirst.visibility = View.VISIBLE
                        randomAnswerLast.visibility = View.VISIBLE
                    }
                    getString(R.string.multiple_selection_custom) -> { //多選題(第一選項, 最後選項)
                        invisibleAll()
                        randomAnswerFirst.visibility = View.VISIBLE
                        randomAnswerLast.visibility = View.VISIBLE
                    }
                    getString(R.string.cloze_custom) -> { //多選項填空(第一選項, 最後選項)
                        invisibleAll()
                        randomAnswerFirst.visibility = View.VISIBLE
                        randomAnswerLast.visibility = View.VISIBLE
                    }
                    getString(R.string.cloze_custom_all) -> { //多選項填空(全部選項)
                        invisibleAll()
                        allSelection.visibility = View.VISIBLE
                        allSelectionHintLabel.visibility = View.VISIBLE
                    }
                    getString(R.string.random_number_max) -> { //隨機數字(最大值)
                        invisibleAll()
                        randomNumberMax.visibility = View.VISIBLE
                    }
                    getString(R.string.random_number_min_max) -> { //隨機數字(最大值, 最小值)
                        invisibleAll()
                        randomNumberMax.visibility = View.VISIBLE
                        randomNumberMin.visibility = View.VISIBLE
                    }
                    else -> {
                        invisibleAll()
                    }
                }
            }
        }

        //自動將RandAnsFirst的文字改成大寫
        randomAnswerFirst.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //如果RandAnsFirst不為空
                if (s.isNotEmpty()) {
                    //先取消監聽
                    randomAnswerFirst.removeTextChangedListener(this)
                    //將第一個字符轉為大寫(因為設定長度最多為1所以只需要設定第1個即可)
                    randomAnswerFirst.setText(s[0].toUpperCase().toString(), TextView.BufferType.EDITABLE)
                    //將輸入位置調至後
                    randomAnswerFirst.setSelection(randomAnswerFirst.length())
                    //重新裝上監聽
                    randomAnswerFirst.addTextChangedListener(this)
                }
            }
        })

        //自動將RandAnsLast的文字改成大寫
        randomAnswerLast.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //如果RandAnsLast不為空
                if (s.isNotEmpty()) {
                    //先取消監聽
                    randomAnswerLast.removeTextChangedListener(this)
                    //將第一個字符轉為大寫(因為設定長度最多為1所以只需要設定第1個即可)
                    randomAnswerLast.setText(s[0].toUpperCase().toString(), TextView.BufferType.EDITABLE)
                    //將輸入位置調至後
                    randomAnswerLast.setSelection(randomAnswerLast.length())
                    //重新裝上監聽
                    randomAnswerLast.addTextChangedListener(this)
                }
            }
        })

        //當按下"生成!"按鈕時
        summonButton.setOnClickListener {
            try {
                //當選中某個選項時
                when (summonTypeSpinner.selectedItem.toString()) {
                    getString(R.string.multiple_choice_A_D) -> {  //單選題(A-D)
                        val rand = ('A'..'D').random().toString()
                        displayAnswer(270f, rand, rand, false)
                    }

                    getString(R.string.multiple_choice_A_E) -> {  //單選題(A-E)
                        val rand = ('A'..'E').random().toString()
                        displayAnswer(270f, rand, rand, false)
                    }

                    getString(R.string.multiple_choice_custom) -> {  //單選題(第一選項, 最後選項)
                        //如果RandAnsFirst和RandAnsLast皆不為空
                        if (randomAnswerFirst.text.isNotEmpty() && randomAnswerLast.text.isNotEmpty()) {
                            val randAnsFirst: Char = randomAnswerFirst.text.toString()[0]
                            val randAnsLast: Char = randomAnswerLast.text.toString()[0]
                            val allAnsArray = ArrayList<Char>()

                            //如果randAnsFirst和randAnsLast都是大寫英文字母
                            if (randAnsFirst.isEnglishLetter() && randAnsLast.isEnglishLetter()) {
                                if (randAnsFirst <= randAnsLast) {  //如果randAnsFirst比randAnsLast小或相同
                                    for (ch: Char in randAnsFirst..randAnsLast) {
                                        allAnsArray.add(ch)
                                    }

                                    val rand = (0 until allAnsArray.size).random()
                                    displayAnswer(270f, allAnsArray[rand].toString(), allAnsArray[rand].toString(), false)
                                } else  //如果randAnsFirst比randAnsLast大
                                //彈出錯誤視窗
                                    showSimpleAlertDialog(getString(R.string.error), getString(R.string.warning_illegal_letter_bound), getString(R.string.confirm))
                            } else {  //如果randAnsFirst和randAnsLast其一不是大寫英文字母
                                //彈出錯誤視窗
                                showSimpleAlertDialog(getString(R.string.error), getString(R.string.warning_english_alphabet), getString(R.string.confirm))
                            }
                        }else {  //如果randAnsFirst和randAnsLast其一為空
                            //彈出錯誤視窗
                            showSimpleAlertDialog(getString(R.string.error), getString(R.string.warning_input_empty), getString(R.string.confirm))
                        }
                    }

                    getString(R.string.multiple_selection_A_D) -> {  //多選題(A-D)
                        val set = randomMultiple(arrayListOf('A', 'B', 'C', 'D'))
                        displayAnswer(90f, set, "[$set]", true)
                    }

                    getString(R.string.multiple_selection_A_E) -> { //多選題(A-E)
                        val set = randomMultiple(arrayListOf('A', 'B', 'C', 'D', 'E'))
                        displayAnswer(90f, set, "[$set]", true)
                    }

                    getString(R.string.multiple_selection_custom) -> {  //多選題(第一選項, 最後選項)
                        //如果RandAnsFirst和RandAnsLast皆不為空
                        if (randomAnswerFirst.text.isNotEmpty() && randomAnswerLast.text.isNotEmpty()) {
                            val randAnsFirst: Char = randomAnswerFirst.text.toString()[0]
                            val randAnsLast: Char = randomAnswerLast.text.toString()[0]
                            val allAnsArray = ArrayList<Char>()

                            //如果randAnsFirst和randAnsLast都是英文字母
                            if (randAnsFirst.isEnglishLetter() && randAnsLast.isEnglishLetter()) {
                                //如果randAnsFirst比randAnsLast小或相同
                                if (randAnsFirst <= randAnsLast) {
                                    //將randAnsFirst到randAnsLast中的所有字母塞到一個ArrayList中
                                    for (ch in randAnsFirst..randAnsLast) {
                                        allAnsArray.add(ch)
                                    }

                                    //生成隨機選項，最少生成1選項，最多全選
                                    val set = randomMultiple(1, randAnsLast - (randAnsFirst - 1), allAnsArray)
                                    displayAnswer(60f, set, "[$set]", true)
                                } else  //如果randAnsFirst比randAnsLast大
                                    showSimpleAlertDialog(getString(R.string.error), getString(R.string.warning_illegal_letter_bound), getString(R.string.confirm))
                            } else
                                showSimpleAlertDialog(getString(R.string.error), getString(R.string.warning_english_alphabet), getString(R.string.confirm))
                        } else {
                            showSimpleAlertDialog(getString(R.string.error), getString(R.string.warning_input_empty), getString(R.string.confirm))
                        }
                    }

                    getString(R.string.cloze_custom) -> {  //多選項填空(第一選項, 最後選項)
                        //如果RandAnsFirst和RandAnsLast皆不為空
                        if (randomAnswerFirst.text.isNotEmpty() && randomAnswerLast.text.isNotEmpty()) {
                            val randAnsFirst: Char = randomAnswerFirst.text.toString()[0]
                            val randAnsLast: Char = randomAnswerLast.text.toString()[0]

                            if (randAnsFirst.isEnglishLetter() && randAnsLast.isEnglishLetter()) {
                                if (randAnsFirst <= randAnsLast) {
                                    val set = cloze(randAnsFirst, randAnsLast)
                                    displayAnswer(30f, set, "[$set]",true)
                                }else
                                    showSimpleAlertDialog(getString(R.string.error), getString(R.string.warning_illegal_letter_bound), getString(R.string.confirm))
                            }else
                                showSimpleAlertDialog(getString(R.string.error), getString(R.string.warning_english_alphabet), getString(R.string.confirm))
                        }else {
                            showSimpleAlertDialog(getString(R.string.error), getString(R.string.warning_input_empty), getString(R.string.confirm))
                        }
                    }

                    getString(R.string.cloze_custom_all) -> {  //多選項填空(全部選項)
                        //如果RandAnsAll不為空
                        if (allSelection.text.isNotEmpty()) {
                            val array = allSelection.text.toString().toCharArrayList()
                            val set = cloze(array)
                            displayAnswer(30f, set, "[$set]", true)
                        }else {  //如果RandAnsAll為空，則彈出錯誤視窗
                            showSimpleAlertDialog(getString(R.string.error), getString(R.string.warning_input_empty), getString(R.string.confirm))
                        }
                    }

                    getString(R.string.yes_or_no) -> {  //是非題
                        val rand = arrayOf("O", "X").random()
                        displayAnswer(270f, rand, rand, false)
                    }

                    getString(R.string.random_number_max) -> {  //最大值隨機數字
                        //如果RandIntMax不為空
                        if (randomNumberMax.text.isNotEmpty()) {
                            //檢測RandIntMax值的範圍
                            when (val randomNumMax = randomNumberMax.text.toString().toInt()) {
                                //若在1~999999999之間，則隨機生成數字
                                in 1..999999999 -> {
                                    val rand = (1..randomNumMax).random()
                                    displayAnswer(60f, rand.toString(), "($rand)", true)
                                }
                                //若小於1，則彈出錯誤視窗
                                in Integer.MIN_VALUE..0 -> {
                                    showSimpleAlertDialog(getString(R.string.error), getString(R.string.warning_lower_than_minimum),getString(R.string.confirm))
                                }
                                //若大於等於1000000000，則彈出錯誤視窗
                                in 1000000000..Integer.MAX_VALUE -> {
                                    showSimpleAlertDialog(getString(R.string.error), getString(R.string.warning_greater_than_maximum), getString(R.string.confirm))
                                }
                            }
                        }else {
                            showSimpleAlertDialog(getString(R.string.error), getString(R.string.warning_input_empty), getString(R.string.confirm))
                        }
                    }

                    getString(R.string.random_number_min_max) -> {  //最小值-最大值隨機數字
                        if (randomNumberMax.text.isNotEmpty() && randomNumberMin.text.isNotEmpty()) {
                            val randomIntMax: Int = randomNumberMax.text.toString().toInt()
                            val randomIntMin: Int = randomNumberMin.text.toString().toInt()
                            if (randomIntMin > randomIntMax)
                                showSimpleAlertDialog(getString(R.string.error), getString(R.string.warning_illegal_bound), getString(R.string.confirm))
                            else if (randomIntMin > 999999999 || randomIntMax > 999999999) {
                                showSimpleAlertDialog(getString(R.string.error), getString(R.string.warning_greater_than_maximum), getString(R.string.confirm))
                            }
                            else {
                                val rand = (randomIntMin..randomIntMax).random()
                                displayAnswer(60f, rand.toString(), "($rand)", true)
                            }
                        }else {
                            showSimpleAlertDialog(getString(R.string.error), getString(R.string.warning_input_empty), getString(R.string.confirm))
                        }
                    }
                    else ->
                        showSimpleAlertDialog(getString(R.string.error), getString(R.string.warning_incorrect_selection), getString(R.string.confirm))
                }
            }catch (err: Throwable) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.error))
                builder.setMessage("$err")
                builder.setCancelable(true)
                builder.setPositiveButton(getString(R.string.confirm)) { _: DialogInterface, _: Int ->}
                builder.show()
            }
        }

        //當按下生成歷史按鈕
        historyButton.setOnClickListener {
            showAlertDialog(getString(R.string.history), summonHistory.toString(), false, 0, true, getString(R.string.confirm), null, false, null, null, true, getString(R.string.copy_to_clipboard),
                {
                        _: DialogInterface, _: Int ->
                    val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText(summonHistory.toString(), summonHistory.toString())
                    clipboard.setPrimaryClip(clip)
                })
        }

        //當按下歸零按鈕
        resetButton.setOnClickListener{
            //彈出警告窗口
            showAlertDialog(getString(R.string.warning), getString(R.string.before_zero_hint), true, android.R.attr.alertDialogIcon, true, getString(R.string.confirm),
                {
                        _: DialogInterface, _: Int ->
                    qNum = 0
                    QNumLabel.text = qNum.toString()
                    summonHistory = StringBuffer()
                    val toast = Toast.makeText(applicationContext, getString(R.string.zeroed), Toast.LENGTH_SHORT)
                    toast.show()
                }, true, getString(R.string.cancel), null, false, null, null)
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
                val finalAlert: AlertDialog = setAlertDialog(getString(R.string.about),
                    null,
                    false,
                    0,
                    true,
                    getString(R.string.confirm),
                    null,
                    false,
                    null,
                    null ,
                    false,
                    null,
                    null).setMessage(R.string.about_string).create()
                finalAlert.show()
                (finalAlert.findViewById(android.R.id.message) as TextView).movementMethod =
                    LinkMovementMethod.getInstance()
            }
            /*R.id.action_save -> {

                    //TODO 存檔系統。 哦好煩哦我懶得做了

                }
            }*/
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * 將所有輸入框隱藏
     */
    fun invisibleAll() {
        randomNumberMax.visibility = View.INVISIBLE
        randomNumberMin.visibility = View.INVISIBLE
        randomAnswerFirst.visibility = View.INVISIBLE
        randomAnswerLast.visibility = View.INVISIBLE
        allSelection.visibility = View.INVISIBLE
        allSelectionHintLabel.visibility = View.INVISIBLE
    }

    /**
     * 顯示新的生成選項，並給題號+1，增加生成歷史
     *
     * @param textSize 設定answerLabel文字大小
     * @param display 設定answerLabel的文字
     * @param history 增加到生成歷史的字符串
     * @param forceNewline 新增生成歷史後強制換行
     */
    private fun displayAnswer(textSize: Float, display: String, history: String, forceNewline: Boolean) {
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
        QNumLabel.text = qNum.toString()
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
     */
    fun showSimpleAlertDialog(title: String?,
                              message: String?,
                              positiveButtonText: String) {
        setAlertDialog(title, message, true, 0, true, positiveButtonText, null, false, null, null, false, null, null).show()
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
            sb.append(Sort.bubble(answer)[i])
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