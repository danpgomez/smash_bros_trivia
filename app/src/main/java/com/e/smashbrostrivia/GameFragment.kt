package com.e.smashbrostrivia

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.e.smashbrostrivia.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    data class Question(val text: String, val answers: List<String>)
    
    // Questions source: https://www.beano.com/posts/ultimate-super-smash-bros-quiz
    private val questions: MutableList<Question> = mutableListOf(
        Question(
            text = "How many playable characters are there in Super Smash Bros?",
            answers = listOf("70+", "150", "55", "24")
        ),
        Question(
            text = "Which of these is NOT a character in Super Smash Bros?",
            answers = listOf("Zorgoth the Impenetrable", "King Dedede", "Lucario", "Inkling")
        ),
        Question(
            text = "What is Mario's home stage?",
            answers = listOf("Peach's Castle", "Saffron City", "Glasgow's East End", "Planet Zebes")
        ),
        Question(
            text = "The lead designer of the series voices King Dedede. True or false?",
            answers = listOf("True", "False", "Maybe", "I designed the game")
        ),
        Question(
            text = "Which of these is a real Super Smash Bros stage?",
            answers = listOf(
                "Port Town Aero Drive",
                "Port Kernow",
                "Palpitation Beach",
                "The Exploding Nose Forest"
            )
        ),
        Question(
            text = "Which of these franchises doesn't appear in Super Smash Bros?",
            answers = listOf("Star Wars", "Mario", "Zelda", "Pokemon")
        ),
        Question(
            text = "What happens after you fire a banana gun?",
            answers = listOf(
                "The gun turns into a banana peel",
                "You increase potassium levels",
                "You automatically fall over",
                "You win the game"
            )
        ),
        Question(
            text = "What kind of animal is Bowser?",
            answers = listOf("A Koopa", "A snail", "A turtle", "A plumber")
        ),
        Question(
            text = "Complete the sentence: 'Princess Peach of the Mushroom ___'",
            answers = listOf("Kingdom", "Empire", "Sultanate", "Car Park")
        ),
        Question(
            text = "What is Pikachu's special move in Super Smash Bros?",
            answers = listOf("Thunder Jolt", "Lightning Zap", "Thunder Blast", "Rain Cloud")
        )
    )

    lateinit var currentQuestion: Question
    lateinit var currentAnswers: MutableList<String>
    private var questionIndex = 0
    private val numberOfQuestions = Math.min((questions.size + 1) / 2, 3)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using the fragment_game.xml
        val binding: FragmentGameBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)

        // Then randomize the questions
        randomizeQuestions()

        // Then bind the fragment_game.xml to this class
        binding.game = this

        binding.submitButton.setOnClickListener { view ->
            val checkedId = binding.answersRadioGroup.checkedRadioButtonId
            // If no answer is selected the id is -1 so we do nothing
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.radioButton_2 -> answerIndex = 1
                    R.id.radioButton_3 -> answerIndex = 2
                    R.id.radioButton_4 -> answerIndex = 3
                }

                // Check whether answer matches correct answer (aka. first answer in original question)
                if (currentAnswers[answerIndex] == currentQuestion.answers[0]) {
                    questionIndex++
                    if (questionIndex < numberOfQuestions) {
                        currentQuestion = questions[questionIndex]
                        setQuestion()
                        binding.invalidateAll()
                    } else {
                        // We won so go to the gameWonFragment
                        view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameWonFragment(numberOfQuestions, questionIndex))
                    }
                } else {
                    // If you get even 1 question wrong you lose. Go to gameOverFragment
                    view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameOverFragment())
                }
            }
        }
        return binding.root
    }

    // Randomize the questions for each new game and set the first question
    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }


    // Sets question and randomizes answers. This only changes the data, not the UI.
    // When we call invalidateAll() on the FragmentGameBinding the UI is reset with the new data
    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        // Copy the answers to a MutableList which randomizes them
        currentAnswers = currentQuestion.answers.toMutableList()
        // And then we shuffle them
        currentAnswers.shuffle()
        // Show number of remaining questions in the title bar
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.smash_bros_trivia_question, questionIndex + 1, numberOfQuestions)
    }
}
