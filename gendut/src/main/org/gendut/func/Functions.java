package org.gendut.func;

import org.gendut.seq.Seq;
import org.gendut.seq.TransformedSeq;


public final class Functions
{
    private Functions()
    {
    }

    @SuppressWarnings("rawtypes")
    private static final Function constantTrue = new NamedFunction("true")
    {
        @Override
        public Object get(Object e)
        {
            return true;
        }
    };

    public static abstract class Predicate<T> implements Function<T, Boolean>
    {
        public static <T> Predicate<T> create(final String name, final Function<T, Boolean> fun)
        {
            return new Predicate<T>()
            {
                @Override
                public Boolean get(T e)
                {
                    return fun.get(e);
                }

                @Override
                public String toString()
                {
                    return name;
                }
            };
        }

        public Predicate<T> and(Predicate<T> other)
        {
            return Functions.and(this, other);
        }

        public Predicate<T> or(Predicate<T> other)
        {
            return Functions.or(this, other);
        }

        public Predicate<T> xor(Predicate<T> other)
        {
            return Functions.xor(this, other);
        }

        public Predicate<T> implies(Predicate<T> other)
        {
            return Functions.implies(this, other);
        }

        public Predicate<T> negate()
        {
            return Functions.negate(this);
        }
    }
    public static abstract class NamedPredicate<T> extends Predicate<T>
    {
        private final String name;

        public NamedPredicate(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }
    public static abstract class NamedFunction<M, N> implements Function<M, N>
    {
        private final String name;

        public NamedFunction(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    @SuppressWarnings("unchecked")
    public static <M> Function<M, Boolean> alwaysTrue()
    {
        return constantTrue;
    };

    @SuppressWarnings("rawtypes")
    private static final Function constantFalse = new NamedFunction("false")
    {
        @Override
        public Object get(Object e)
        {
            return false;
        }
    };

    @SuppressWarnings("unchecked")
    public static <M> Function<M, Boolean> alwaysFalse()
    {
        return constantFalse;
    };

    public static <M> Predicate<M> negate(final Function<M, Boolean> f)
    {
        return new Predicate<M>()
        {
            @Override
            public Boolean get(M e)
            {
                return !f.get(e);
            }

            @Override
            public String toString()
            {
                return "-(" + f + ")";
            }
        };
    }

    public static <M> Predicate<M> and(final Function<M, Boolean> f, final Function<M, Boolean> g)
    {
        return new Predicate<M>()
        {
            @Override
            public Boolean get(M e)
            {
                return f.get(e) && g.get(e);
            }

            @Override
            public String toString()
            {
                return "(" + f + ")&&(" + g + ")";
            }
        };
    }

    public static <M> Predicate<M> or(final Function<M, Boolean> f, final Function<M, Boolean> g)
    {
        return new Predicate<M>()
        {
            @Override
            public Boolean get(M e)
            {
                return f.get(e) || g.get(e);
            }

            @Override
            public String toString()
            {
                return "(" + f + ")||(" + g + ")";
            }
        };
    }

    public static <M> Predicate<M> xor(final Function<M, Boolean> f, final Function<M, Boolean> g)
    {
        return new Predicate<M>()
        {
            @Override
            public Boolean get(M e)
            {
                return f.get(e) ^ g.get(e);
            }

            @Override
            public String toString()
            {
                return "(" + f + ")||(" + g + ")";
            }
        };
    }

    public static <M> Predicate<M> implies(final Function<M, Boolean> f, final Function<M, Boolean> g)
    {
        return new Predicate<M>()
        {
            @Override
            public Boolean get(M e)
            {
                return (!f.get(e)) || g.get(e);
            }

            @Override
            public String toString()
            {
                return "(" + f + ")=>(" + g + ")";
            }
        };
    }

    @SuppressWarnings("rawtypes")
    private static final Function id = new Function()
    {
        @Override
        public Object get(Object e)
        {
            return e;
        }

        @Override
        public String toString()
        {
            return "id";
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> Function<T, T> id()
    {
        return id;
    }

    static public <M, N> Function<Seq<M>, Seq<N>> lift(final Function<M, N> elementFun)
    {
        return new Function<Seq<M>, Seq<N>>()
        {
            @Override
            public Seq<N> get(Seq<M> input)
            {
                return TransformedSeq.create(input, elementFun);
            }

            @Override
            public String toString()
            {
                return "lift(" + elementFun + ")";
            }
        };
    }

    static public <M> Function<Seq<M>, Seq<M>> filter(final Function<M, Boolean> condition)
    {
        return new Function<Seq<M>, Seq<M>>()
        {
            @Override
            public Seq<M> get(Seq<M> input)
            {
                Function<M, M> idM = id();
                return TransformedSeq.create(input, condition, idM);
            }

            @Override
            public String toString()
            {
                return "filter(" + condition + ")";
            }
        };
    }

    static public <M, N, O> Function<M, O> multiply(final Function<M, N> f1, final Function<N, O> f2)
    {
        return new Function<M, O>()
        {
            @Override
            public O get(M e)
            {
                return f2.get(f1.get(e));
            }

            @Override
            public String toString()
            {
                return "" + f2 + "(" + f1 + ")";
            }
        };
    }
}
