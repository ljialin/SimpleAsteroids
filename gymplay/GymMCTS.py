#!/usr/bin/env python2

# Seemingly Buggy MCTS code from here: https://gist.github.com/blole/dfebbec182e6b72ec16b66cc7e331110
# but useful example of how to copy state in OpenAIGym
# also useful example if using carriage return instead of new line to print in overwrite mode on a single line


import os
import gym
import sys
import random
import itertools
from time import time
from copy import copy
from math import sqrt, log


def moving_average(v, n):
    n = min(len(v), n)
    ret = [.0]*(len(v)-n+1)
    ret[0] = float(sum(v[:n]))/n
    for i in range(len(v)-n):
        ret[i+1] = ret[i] + float(v[n+i] - v[i])/n
    return ret


def ucb(node):
    return node.value / node.visits + sqrt(log(node.parent.visits)/node.visits)


def combinations(space):
    if isinstance(space, gym.spaces.Discrete):
        return range(space.n)
    elif isinstance(space, gym.spaces.Tuple):
        return itertools.product(*[combinations(s) for s in space.spaces])
    else:
        raise NotImplementedError


class Node:
    def __init__(self, parent, action):
        self.parent = parent
        self.action = action
        self.children = []
        self.explored_children = 0
        self.visits = 0
        self.value = 0


class Runner:
    def __init__(self, rec_dir, env_name, loops=10, max_depth=10, playouts=100):
        self.env_name = env_name
        self.dir = rec_dir+'/'+env_name

        self.loops = loops
        self.max_depth = max_depth
        self.playouts = playouts

    def print_stats(self, loop, score, avg_time, itr):
        sys.stdout.write('\r%3d   score:%10.3f   avg_time:%4.1f s, %3d' % (loop, score, avg_time, itr))
        sys.stdout.flush()

    def print_decision(self, itr, decision):
        sys.stdout.write('\r%3d   \t %3d' % (itr, decision))
        sys.stdout.flush()

    def run(self):
        best_rewards = []
        start_time = time()
        env = gym.make(self.env_name)
        env.render()
        print (self.env_name)


        #env.

        #  env.monitor was used to log states but is no longer suppoted
        #  see:  https://github.com/carpedm20/deep-rl-tensorflow/issues/22

        #env.monitor.start(self.dir)



        for loop in range(self.loops):
            env.reset()
            root = Node(None, None)

            best_actions = []
            best_reward = float("-inf")

            for i in range(self.playouts):
                #print(i)

                # STATE Copy is below
                state = copy(env)
                # del state._monitor

                self.print_stats(loop + 1, -1, -1, i)

                sum_reward = 0
                node = root
                terminal = False
                actions = []

                # selection
                while node.children:
                    if node.explored_children < len(node.children):
                        child = node.children[node.explored_children]
                        node.explored_children += 1
                        node = child
                    else:
                        node = max(node.children, key=ucb)
                    _, reward, terminal, _ = state.step(node.action)
                    sum_reward += reward
                    actions.append(node.action)

                # expansion
                if not terminal:
                    node.children = [Node(node, a) for a in combinations(state.action_space)]
                    random.shuffle(node.children)

                # playout
                while not terminal:
                    action = state.action_space.sample()
                    _, reward, terminal, _ = state.step(action)
                    sum_reward += reward
                    actions.append(action)

                    if len(actions) > self.max_depth:
                        sum_reward -= 100
                        break

                # remember best
                if best_reward < sum_reward:
                    best_reward = sum_reward
                    best_actions = actions

                # backpropagate
                while node:
                    node.visits += 1
                    node.value += sum_reward
                    node = node.parent

                # fix monitors not being garbage collected
                # del state._monitor

            sum_reward = 0
            for action in best_actions:
                _, reward, terminal, _ = env.step(action)
                env.render()
                sum_reward += reward
                if terminal:
                    break

            env.render()
            best_rewards.append(sum_reward)
            score = max(moving_average(best_rewards, 100))
            avg_time = (time()-start_time)/(loop+1)
            self.print_stats(loop+1, score, avg_time, 0)

        #  env.monitor.close()
        env.render(self, mode='human', close=True)
        # env.render
        print


def main():
    # get rec_dir
    if not os.path.exists('rec'):
        os.makedirs('rec')
        next_dir = 0
    else:
        next_dir = max([int(f) for f in os.listdir('rec')+["0"] if f.isdigit()])+1
    rec_dir = 'rec/'+str(next_dir)
    os.makedirs(rec_dir)
    print ("rec_dir:", rec_dir)

    # Toy text
    Runner(rec_dir, 'Alien-v0',   loops=10, playouts=100, max_depth=20).run()

    #Runner(rec_dir, 'Taxi-v1',   loops=100, playouts=4000, max_depth=50).run()
    # Runner(rec_dir, 'NChain-v0', loops=100, playouts=3000, max_depth=50).run()

    # Algorithmic
    # Runner(rec_dir, 'Copy-v0').run()
    # Runner(rec_dir, 'RepeatCopy-v0').run()
    # Runner(rec_dir, 'DuplicatedInput-v0').run()
    # Runner(rec_dir, 'ReversedAddition-v0').run()
    # Runner(rec_dir, 'ReversedAddition3-v0').run()
    # Runner(rec_dir, 'Reverse-v0').run()


if __name__ == "__main__":
    main()
