package com.rahma.calculater

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.doOnTextChanged
import bsh.Interpreter
import com.rahma.calculater.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calculate()
    }

    private fun calculate() {
        val observable = Observable.create<String> { emitor ->
            binding.inputText.doOnTextChanged { text, _, _, _ ->
                emitor.onNext(text.toString())
            }
        }
        observable.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).debounce((1.5).toLong(), TimeUnit.SECONDS).subscribe({ t ->
                Log.i(TAG, "on next: ${Thread.currentThread().name}")
                binding.Result.text = operator(t).toString()
            },
                { e ->
                    binding.Result.text = e.message
                })

    }


private fun operator(value: String): Any? {
    val interpreter = Interpreter()
    interpreter.eval(value)
    return interpreter.get("result")

}


    companion object{
        const val TAG="Rahma_Ahmed"
    }
}